package ar.edu.utn.frba.dds.model.notificaciones;

import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;
import ar.edu.utn.frba.dds.repositories.NotificacionRepository;

/** Envio sincronico. Para procesos batch, donde no hay request que desbloquear. */
public class Notificador implements EnviadorNotificaciones {

  private final NotificacionRepository repositorio;

  public Notificador() {
    this(NotificacionRepository.Instance);
  }

  public Notificador(NotificacionRepository repositorio) {
    this.repositorio = repositorio;
  }

  @Override
  public Notificacion enviarNotificacionA(Destinatario destinatario, String mensajeTexto) {
    MedioContacto destino = destinatario.medioDeContacto();

    if (destino == null) {
      throw new IllegalStateException(
          "'" + destinatario.nombreParaMostrar() + "' no tiene medio de contacto configurado.");
    }

    Notificacion notificacion = new Notificacion(mensajeTexto);
    try {
      destino.contactar(notificacion);
    } finally {
      repositorio.registrar(notificacion);
    }
    return notificacion;
  }
}










