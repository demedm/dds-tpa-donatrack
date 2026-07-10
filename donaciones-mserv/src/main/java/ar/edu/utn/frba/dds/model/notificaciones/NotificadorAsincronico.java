package ar.edu.utn.frba.dds.model.notificaciones;

import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;
import ar.edu.utn.frba.dds.repositories.NotificacionRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Se instancia una sola vez por microservicio (ver Bootstrap) para no
 * crear un pool de hilos nuevo por cada notificación.
 */
public class NotificadorAsincronico implements EnviadorNotificaciones {

  private final ExecutorService pool;
  private final NotificacionRepository repositorio;

  public NotificadorAsincronico(NotificacionRepository repositorio) {
    // Pool chico y fijo: alcanza para I/O de notificaciones, no hace falta
    // más para el volumen de un TP. Threads daemon para no trabar el shutdown.
    this.pool = Executors.newFixedThreadPool(4, runnable -> {
      Thread hilo = new Thread(runnable, "notificador-async");
      hilo.setDaemon(true);
      return hilo;
    });
    this.repositorio = repositorio;
  }

  @Override
  public Notificacion enviarNotificacionA(Persona persona, String mensajeTexto) {
    Notificacion notificacion = new Notificacion(mensajeTexto);
    repositorio.registrar(notificacion);

    pool.submit(() -> {
      try {
        MedioContacto destino = persona.getMedioPreferido();
        if (destino == null) {
          notificacion.marcarComoFallida();
          System.err.println("[Notificaciones] '" + persona.getNombreIdentificador()
              + "' no tiene medio de contacto preferido configurado.");
          return;
        }
        destino.contactar(notificacion);
      } catch (Exception e) {
        notificacion.marcarComoFallida();
        System.err.println("[Notificaciones] Error al notificar a "
            + persona.getNombreIdentificador() + ": " + e.getMessage());
      }
    });

    return notificacion;
  }

  @Override
  public Notificacion enviarNotificacionA(MedioContacto medio, String mensajeTexto) {
    Notificacion notificacion = new Notificacion(mensajeTexto);
    repositorio.registrar(notificacion);

    pool.submit(() -> {
      try {
        medio.contactar(notificacion);
      } catch (Exception e) {
        notificacion.marcarComoFallida();
        System.err.println("[Notificaciones] Error al notificar a "
            + medio.getMedioContacto() + ": " + e.getMessage());
      }
    });

    return notificacion;
  }
}
