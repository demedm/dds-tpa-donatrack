package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.UsuarioRepositorio;
import io.javalin.http.Context;

public class UsuarioController {
  public Usuario showUsuario(Context ctx) {
    var idUsuario = ctx.pathParam("id");
    if (idUsuario.isEmpty()) {
      ctx.status(400);
      return null;
    }
    var usuario = UsuarioRepositorio.Instance.findUsuarioById(idUsuario);
    if (usuario == null) {
      ctx.status(404);
      return null;
    }
    ctx.status(200);
    return usuario;
  }

  public void confirmarEntrega(Context ctx) {
    var idUsuario = ctx.pathParam("idUsuario");
    var idEntrega = ctx.pathParam("idEntrega");
    var urlfoto = ctx.body();
    if (urlfoto.isEmpty()) {
      ctx.status(400);
      return;
    }
    var entidad = UsuarioRepositorio.Instance.findEntidadById(idUsuario);
    if (entidad == null) {
      ctx.status(404);
      return;
    }
    entidad.confirmarEntrega(idEntrega, urlfoto);
    ctx.status(200);
    ctx.json(entidad);
  }

}
