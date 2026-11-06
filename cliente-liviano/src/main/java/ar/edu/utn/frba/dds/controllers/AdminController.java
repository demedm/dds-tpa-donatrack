package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AdminController {
  public void registrarDonante(@NotNull Context ctx) {
  }

  public void administracion(@NotNull Context ctx) {
    Map<String, Object> modelo = new HashMap<>();
    UsuarioLogueado usuario = ctx.attribute("usuarioLogueado");
    System.out.println("Usuario autenticado: " + usuario);
    modelo.put("logueado", usuario != null);
    ctx.render("admin/administracion.hbs");
  }
}
