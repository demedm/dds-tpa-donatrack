package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import ar.edu.utn.frba.dds.session.Rol;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;

public class RolMiddleware {
  public static Rol rolPermitido;

  public RolMiddleware(Rol rolPermitido) {
    this.rolPermitido = rolPermitido;
  }

  public void validarPermisos(Context ctx) {
    UsuarioLogueado usuario = ctx.attribute("usuarioLogueado");

    if (usuario == null) {
      ctx.status(403);
      ctx.redirect("/login");
      return;
    }

    if (!rolPermitido.equals(usuario.getRol())) {
      throw new ForbiddenResponse("No tenés permisos para acceder a esta página.");
    }

  }

}
