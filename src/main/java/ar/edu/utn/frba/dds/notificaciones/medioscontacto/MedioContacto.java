package ar.edu.utn.frba.dds.notificaciones.medioscontacto;

import ar.edu.utn.frba.dds.notificaciones.Notificacion;

public interface MedioContacto {

  public String getMedioContacto();

  void contactar(Notificacion notificacion);

}
