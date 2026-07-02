package ar.edu.utn.frba.dds.medioscontacto;

import ar.edu.utn.frba.dds.Notificacion;

public interface MedioContacto {

  public String getMedioContacto();

  void contactar(Notificacion notificacion);

}
