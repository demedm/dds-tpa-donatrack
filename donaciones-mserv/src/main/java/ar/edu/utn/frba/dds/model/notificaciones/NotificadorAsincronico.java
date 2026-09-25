package ar.edu.utn.frba.dds.model.notificaciones;

import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;
import ar.edu.utn.frba.dds.repositories.EntityManagerHelper;
import ar.edu.utn.frba.dds.repositories.NotificacionRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Envio en segundo plano, para no bloquear el request HTTP.
 * NO es una cola de mensajes: no persiste los mensajes, no garantiza entrega,
 * aca unicamente "no bloquea el request".
 */
public class NotificadorAsincronico implements EnviadorNotificaciones {

  private final ExecutorService pool;
  private final NotificacionRepository repositorio;

  public NotificadorAsincronico() {
    this(NotificacionRepository.Instance, 4);
  }

  public NotificadorAsincronico(NotificacionRepository repositorio, int cantidadDeHilos) {
    this.repositorio = repositorio;
    this.pool = Executors.newFixedThreadPool(cantidadDeHilos, runnable -> {
      Thread hilo = new Thread(runnable, "notificador-async");
      hilo.setDaemon(true);
      return hilo;
    });
  }

  @Override
  public Notificacion enviarNotificacionA(Destinatario destinatario, String mensajeTexto) {
    Notificacion notificacion = new Notificacion(mensajeTexto);
    repositorio.registrar(notificacion);

    pool.submit(() -> {
      try {
        MedioContacto destino = destinatario.medioDeContacto();
        if (destino == null) {
          notificacion.marcarComoFallida();
          System.err.println("[Notificaciones] '" + destinatario.nombreParaMostrar()
              + "' no tiene medio de contacto configurado.");
          return;
        } else {
          destino.contactar(notificacion);
        }
      } catch (Exception e) {
        notificacion.marcarComoFallida();
        System.err.println("[Notificaciones] Error al notificar a "
            + destinatario.nombreParaMostrar() + ": " + e.getMessage());
      } finally {
        try {
          repositorio.actualizar(notificacion);
        } catch (Exception e) {
          System.err.println("[Notificaciones] No se pudo guardar el estado: " + e.getMessage());
        }
        EntityManagerHelper.closeEntityManager();
      }
    });
    return notificacion;
    }
    public void cerrar() {
      pool.shutdown();
    }
  }

