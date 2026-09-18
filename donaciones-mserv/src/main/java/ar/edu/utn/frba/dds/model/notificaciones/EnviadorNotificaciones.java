package ar.edu.utn.frba.dds.model.notificaciones;

public interface EnviadorNotificaciones {

  Notificacion enviarNotificacionA(Destinatario destinatario, String mensajeTexto);
}

