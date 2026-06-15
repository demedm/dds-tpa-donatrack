package ar.edu.utn.frba.dds;

public class Notificador {

  public Notificacion enviarNotificacionA(Entidad entidad, String mensajeTexto) {

    MedioContacto destino = entidad.getTipoContactoParaNotificaciones();

    Notificacion notificacion = new Notificacion(mensajeTexto);

    destino.contactar(notificacion);

    return notificacion;
  }
}

//corroborar si va a ser void o no, segun como se manejen los test
