package ar.edu.utn.frba.dds.controllers.autenticacion;

import io.javalin.security.RouteRole;

public enum Rol implements RouteRole {
  PUBLIC,
  DONANTE,
  BENEFICIARIO,
  ADMIN
}
