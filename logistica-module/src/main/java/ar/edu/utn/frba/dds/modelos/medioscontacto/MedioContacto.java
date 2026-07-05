package ar.edu.utn.frba.dds.modelos.medioscontacto;

import ar.edu.utn.frba.dds.Notificacion;

public interface MedioContacto {

  public String getMedioContacto();

  void contactar(Notificacion notificacion);

}
