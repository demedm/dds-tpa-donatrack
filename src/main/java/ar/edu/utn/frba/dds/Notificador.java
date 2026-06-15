package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.donantes.Donante;
import ar.edu.utn.frba.dds.medioscontacto.MedioContacto;

public class Notificador {
  public Notificacion enviarNotificacionA(Donante donante, String mensajeTexto) {

    MedioContacto destino = donante.getMedioPreferido();

    if (destino == null) {
      throw new IllegalStateException("El donante '" + donante.getNombre() + "' no posee un medio de contacto preferido configurado.");
    }

    Notificacion notificacion = new Notificacion(mensajeTexto);

    destino.contactar(notificacion);

    return notificacion;
  }
}


