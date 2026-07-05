package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.donantes.Persona;
import ar.edu.utn.frba.dds.medioscontacto.MedioContacto;
import org.springframework.stereotype.Component;

@Component
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


