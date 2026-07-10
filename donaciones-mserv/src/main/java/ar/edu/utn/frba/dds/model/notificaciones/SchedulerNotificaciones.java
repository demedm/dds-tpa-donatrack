package ar.edu.utn.frba.dds.model.notificaciones;

import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.repositories.DonanteRepository;
import ar.edu.utn.frba.dds.repositories.NotificacionRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Tarea calendarizada que detecta donantes inactivos y les envía una
 * notificación. Reemplaza al viejo InactividadDonantesScheduler, que estaba
 * escrito con anotaciones de Spring (@Component, @Scheduled, @Value) y no
 * corría en este proyecto porque el servidor es Javalin puro.
 *
 * Se ejecuta todos los días a las 02:00 AM (horario de baja carga) para no
 * degradar el desempeño del sistema durante las horas pico, tal como pide
 * el enunciado. Usa un ScheduledExecutorService de la librería estándar de
 * Java: no hace falta agregar Quartz ni ninguna dependencia nueva al pom.
 *
 * Se arranca una sola vez, desde Bootstrap.init(), cuando levanta el server.
 */
public class SchedulerNotificaciones {

  public static final SchedulerNotificaciones Instance = new SchedulerNotificaciones();

  private static final int HORA_EJECUCION = 2; // 02:00 AM
  private static final int DIAS_INACTIVIDAD = 20;

  private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(runnable -> {
    Thread hilo = new Thread(runnable, "scheduler-notificaciones");
    hilo.setDaemon(true);
    return hilo;
  });

  private final EventoNotificacionService eventoService;

  private SchedulerNotificaciones() {
    this.eventoService = new EventoNotificacionService(
        new NotificadorAsincronico(NotificacionRepository.Instance));
  }

  /** Programa la ejecución diaria. Idempotente: si se llama dos veces, no duplica la tarea. */
  public void iniciar() {
    long demoraInicial = segundosHastaProximaEjecucion();
    long periodo = TimeUnit.DAYS.toSeconds(1);

    executor.scheduleAtFixedRate(this::notificarDonantesInactivos,
        demoraInicial, periodo, TimeUnit.SECONDS);

    System.out.println("[Scheduler] Notificación de donantes inactivos programada. "
        + "Próxima ejecución en " + demoraInicial + " segundos (02:00 AM).");
  }

  /** Expuesto para poder dispararlo manualmente desde un test, sin esperar al horario real. */
  void notificarDonantesInactivos() {
    try {
      System.out.println("[Scheduler] Buscando donantes sin actividad hace más de "
          + DIAS_INACTIVIDAD + " días...");

      LocalDate limiteInactividad = LocalDate.now().minusDays(DIAS_INACTIVIDAD);
      List<Persona> inactivos = DonanteRepository.Instance.buscarInactivosDesde(limiteInactividad);

      if (inactivos == null || inactivos.isEmpty()) {
        System.out.println("[Scheduler] No hay donantes inactivos para notificar.");
        return;
      }

      int enviadas = 0;
      int fallidas = 0;
      for (Persona donante : inactivos) {
        try {
          eventoService.notificarInactividadDonante(donante);
          enviadas++;
        } catch (Exception e) {
          fallidas++;
          System.err.println("[Scheduler] Error al notificar a '"
              + donante.getNombreIdentificador() + "': " + e.getMessage());
        }
      }

      System.out.println("[Scheduler] Completado. Notificaciones encoladas: "
          + enviadas + ", fallidas al encolar: " + fallidas);
    } catch (Exception e) {
      // Nunca dejamos que una excepción mate al hilo del scheduler: si pasa,
      // simplemente no corrió hoy y se reintenta mañana a la misma hora.
      System.err.println("[Scheduler] Error inesperado en la corrida diaria: " + e.getMessage());
    }
  }

  private long segundosHastaProximaEjecucion() {
    LocalDateTime ahora = LocalDateTime.now();
    LocalDateTime proxima = ahora.withHour(HORA_EJECUCION).withMinute(0).withSecond(0).withNano(0);

    if (!proxima.isAfter(ahora)) {
      proxima = proxima.plusDays(1);
    }

    return Duration.between(ahora, proxima).getSeconds();
  }
}
