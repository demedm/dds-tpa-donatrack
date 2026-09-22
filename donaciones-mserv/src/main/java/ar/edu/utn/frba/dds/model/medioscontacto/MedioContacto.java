package ar.edu.utn.frba.dds.model.medioscontacto;

import ar.edu.utn.frba.dds.model.notificaciones.Notificacion;
import ar.edu.utn.frba.dds.model.notificaciones.Destinatario;

public interface MedioContacto extends Destinatario {

  String getMedioContacto();

  void contactar(Notificacion notificacion);

  @Override
  default MedioContacto medioDeContacto() {
    return this;
  }

  @Override
  default String nombreParaMostrar() {
    return getMedioContacto();
  }

}
