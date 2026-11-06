package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import ar.edu.utn.frba.dds.session.Rol;
import io.javalin.http.Context;

public class RolMiddleware {
  public static Rol rolPermitido;

  public RolMiddleware(Rol rolPermitido) {
    this.rolPermitido = rolPermitido;
  }

  public void validarPermisos(Context ctx) {
    UsuarioLogueado usuario = ctx.attribute("usuarioLogueado");

    if (usuario == null) {
      ctx.status(403);
      return;
    }

    if (!rolPermitido.name().equals(usuario.getRol())) {
      ctx.status(403).result("No tenes permisos para acceder a esta página.");
    }

  }

}
