package ar.edu.utn.frba.dds.model.donantes;

import ar.edu.utn.frba.dds.model.usuarios.Rol;

public enum MedioPreferido {
  MAIL("MAIL"),
  TELEFONO("TELEFONO");

  private final String nombre;
  MedioPreferido(String nombre) {
    this.nombre = nombre;
  }

  public static MedioPreferido strTo(String str) {
    for (MedioPreferido r : values()) {
      if (r.nombre.equals(str)) {
        return r;
      }
    }
    throw new RuntimeException("El nombre pasado no corresponde con un medio de contacto.");
  }
}
