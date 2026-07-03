package ar.edu.utn.frba.dds.necesidad;

import ar.edu.utn.frba.dds.EventoNotificacionService;
import ar.edu.utn.frba.dds.RegistroDonante;
import ar.edu.utn.frba.dds.donantes.Persona;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Tarea calendarizada que detecta donantes inactivos y les envía una notificación.
 *
 * Se ejecuta todos los días a las 02:00 AM (horario de baja carga) para no
 * degradar el desempeño del sistema, tal como pide el TP.
 *
 * Decisión de diseño: se usa ScheduledExecutorService de Java puro en lugar
 * de @Scheduled de Spring, ya que el proyecto no tiene una clase Application
 * que arranque el contexto de Spring. Esto cumple el requerimiento de
 * "ejecución calendarizada" sin necesitar un entry point de Spring Boot.
 */
public class InactividadDonantesScheduler {

  private static final int DIAS_INACTIVIDAD = 20;
  private static final LocalTime HORA_EJECUCION = LocalTime.of(2, 0); // 02:00 AM
  private static final ZoneId ZONA = ZoneId.of("America/Argentina/Buenos_Aires");

  private final EventoNotificacionService eventoService;
  private final RegistroDonante registroDonante;
  private final ScheduledExecutorService executor;

  public InactividadDonantesScheduler(EventoNotificacionService eventoService,
                                      RegistroDonante registroDonante) {
    this.eventoService = eventoService;
    this.registroDonante = registroDonante;
    this.executor = Executors.newSingleThreadScheduledExecutor();
  }

  /**
   * Inicia la tarea calendarizada.
   * Calcula el tiempo hasta las 02:00 AM y programa la ejecución diaria.
   * Debe llamarse al iniciar la aplicación.
   */
  public void iniciar() {
    long segundosHastaEjecucion = calcularDemoraSiguienteEjecucion();

    executor.scheduleAtFixedRate(
        this::notificarDonantesInactivos,
        segundosHastaEjecucion,
        TimeUnit.DAYS.toSeconds(1),
        TimeUnit.SECONDS
    );

    System.out.println("[SCHEDULER] Iniciado. Primera ejecución en "
        + (segundosHastaEjecucion / 3600) + " horas.");
  }

  /**
   * Detiene la tarea calendarizada limpiamente.
   * Debe llamarse al apagar la aplicación.
   */
  public void detener() {
    executor.shutdown();
    System.out.println("[SCHEDULER] Detenido.");
  }

  /**
   * Lógica principal: busca donantes inactivos y dispara las notificaciones.
   * Visibilidad package-private para que los tests puedan invocarlo directamente
   * sin esperar el horario real.
   */
  void notificarDonantesInactivos() {
    System.out.println("[SCHEDULER] Buscando donantes sin actividad hace más de "
        + DIAS_INACTIVIDAD + " días...");

    LocalDate limite = LocalDate.now().minusDays(DIAS_INACTIVIDAD);
    List<Persona> inactivos = registroDonante.buscarInactivosDesde(limite);

    if (inactivos.isEmpty()) {
      System.out.println("[SCHEDULER] No hay donantes inactivos.");
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
        System.err.println("[SCHEDULER] Error al notificar a '"
            + donante.getNombreIdentificador() + "': " + e.getMessage());
      }
    }

    System.out.println("[SCHEDULER] Completado. Enviadas: " + enviadas
        + ", Fallidas: " + fallidas);
  }

  private long calcularDemoraSiguienteEjecucion() {
    ZonedDateTime ahora = ZonedDateTime.now(ZONA);
    ZonedDateTime proxima = ahora.toLocalDate().atTime(HORA_EJECUCION).atZone(ZONA);

    if (!ahora.isBefore(proxima)) {
      proxima = proxima.plusDays(1);
    }

    return proxima.toEpochSecond() - ahora.toEpochSecond();
  }
}