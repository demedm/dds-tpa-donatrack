package ar.edu.utn.frba.dds.model.medioscontacto;

import ar.edu.utn.frba.dds.model.notificaciones.Notificacion;

public interface MedioContacto {

  public String getMedioContacto();

  void contactar(Notificacion notificacion);

}
