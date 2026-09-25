package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.UsuarioRepositorio;
import ar.edu.utn.frba.dds.scripts.dto.ConfirmacionEntregaDto;
import io.javalin.http.Context;

public class UsuarioController {
  public Usuario showUsuario(Context ctx) {
    var idUsuario = Long.parseLong(ctx.pathParam("id"));
    if (idUsuario != 0) {
      ctx.status(400);
      return null;
    }
    var usuario = UsuarioRepositorio.Instance.buscarPorId(idUsuario);
    if (usuario == null) {
      ctx.status(404);
      return null;
    }
    ctx.status(200);
    return usuario;
  }

  public void confirmarEntrega(Context ctx) {
    var idUsuario = Long.parseLong(ctx.pathParam("idUsuario"));
    var idEntrega = Long.parseLong(ctx.pathParam("id"));
    var confirmacion = ctx.bodyAsClass(ConfirmacionEntregaDto.class);

    var entidad = UsuarioRepositorio.Instance.buscarEntidadBeneficiariaPorId(idUsuario);

    UsuarioRepositorio.Instance.confirmarEntrega(entidad, idEntrega, confirmacion);
    ctx.status(200);
  }

  public void marcarEntregaComoNoRecepcionada(Context ctx) {
    var idUsuario = Long.parseLong(ctx.pathParam("idUsuario"));
    var idEntrega = Long.parseLong(ctx.pathParam("id"));
    var entidad = UsuarioRepositorio.Instance.buscarEntidadBeneficiariaPorId(idUsuario);
    UsuarioRepositorio.Instance.noRecepcionaEntrega(entidad, idEntrega);
    ctx.status(200);
  }

}
