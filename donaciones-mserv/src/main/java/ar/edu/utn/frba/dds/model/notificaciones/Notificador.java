package ar.edu.utn.frba.dds.model.notificaciones;

import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;
import ar.edu.utn.frba.dds.repositories.NotificacionRepository;

/* Envio sincrono, es decir, va a bloquearse hasta que el medio de contacto termina de contactar */
public class Notificador implements EnviadorNotificaciones {
  private final NotificacionRepository repositorio;

  public Notificador() {
    this(NotificacionRepository.Instance);
  }

  public Notificador(NotificacionRepository repositorio) {
    this.repositorio = repositorio;
  }

  @Override
  public Notificacion enviarNotificacionA(Persona persona, String mensajeTexto) {
    MedioContacto destino = persona.getMedioPreferido();

    if (destino == null) {
      throw new IllegalStateException(
          "El donante '" + persona.getNombreIdentificador()
              + "' no posee un medio de contacto preferido configurado.");
    }

    return enviarNotificacionA(destino, mensajeTexto);
  }

  @Override
  public Notificacion enviarNotificacionA(MedioContacto medio, String mensajeTexto) {
    Notificacion notificacion = new Notificacion(mensajeTexto);
    medio.contactar(notificacion);
    repositorio.registrar(notificacion);
    return notificacion;
  }
}

