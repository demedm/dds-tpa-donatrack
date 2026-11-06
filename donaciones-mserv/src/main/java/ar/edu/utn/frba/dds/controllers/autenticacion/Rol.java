package ar.edu.utn.frba.dds.controllers.autenticacion;

import io.javalin.security.RouteRole;

public enum Rol implements RouteRole {
  PUBLIC("PUBLIC"),
  DONANTE("DONANTE"),
  BENEFICIARIO("BENEFICIARIO"),
  ADMIN("ADMIN");

  private final String nombre;
  Rol(String nombre) {
    this.nombre = nombre;
  }

  public static Rol strTo(String str) {
    for (Rol r : values()) {
      if (r.nombre.equals(str)) {
        return r;
      }
    }
    throw new RuntimeException("El nombre pasado no corresponde con un rol.");
  }
}
