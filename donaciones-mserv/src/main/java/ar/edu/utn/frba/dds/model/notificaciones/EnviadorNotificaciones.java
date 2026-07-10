package ar.edu.utn.frba.dds.model.notificaciones;

import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;

public interface EnviadorNotificaciones {

  Notificacion enviarNotificacionA(Persona persona, String mensajeTexto);

  Notificacion enviarNotificacionA(MedioContacto medio, String mensajeTexto);
}

