package ar.edu.utn.frba.dds.model.notificaciones;

import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;

/**
 * Algo a lo que se le puede mandar una notificacion: una Persona, una
 * EntidadBeneficiaria, o un MedioContacto suelto.
 *
 * Evita que EventoNotificacionService necesite una sobrecarga por cada tipo
 * de destinatario con el mismo texto duplicado.
 */
public interface Destinatario {

  /** Puede ser null si no hay ninguno configurado. */
  MedioContacto medioDeContacto();

  /** Como nombrarlo en el cuerpo del mensaje. */
  String nombreParaMostrar();
}
