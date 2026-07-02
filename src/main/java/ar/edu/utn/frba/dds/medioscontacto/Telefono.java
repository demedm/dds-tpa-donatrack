package ar.edu.utn.frba.dds.medioscontacto;

import ar.edu.utn.frba.dds.Notificacion;

public class Telefono implements MedioContacto {
  public String nroTelefono;

  public Telefono(String telefono) {
    nroTelefono = telefono;
  }

  @Override
  public String getMedioContacto() {
    return nroTelefono;
  }

  @Override
  public void contactar(Notificacion notificacion) {
  }
}
//creeria que con la clase mensaje ya estaria