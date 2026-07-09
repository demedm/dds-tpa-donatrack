package ar.edu.utn.frba.dds.model.notificaciones;

import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;


public class Notificador {
  public Notificacion enviarNotificacionA(Persona persona, String mensajeTexto) {

    MedioContacto destino = persona.getMedioPreferido();

    if (destino == null) {
      throw new IllegalStateException("El donante '" + persona.getNombreIdentificador() + "' no posee un medio de contacto preferido configurado.");
    }

    Notificacion notificacion = new Notificacion(mensajeTexto);

    destino.contactar(notificacion);

    return notificacion;
  }
}


