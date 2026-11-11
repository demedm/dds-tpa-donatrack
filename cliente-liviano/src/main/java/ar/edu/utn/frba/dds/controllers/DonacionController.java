package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import ar.edu.utn.frba.dds.repositorios.JwtRepositorio;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class DonacionController {

  public void mostrarRegistroDonacion(Context ctx) {
    String token = ctx.cookie("session");
    if (token == null || token.isBlank()) {
      ctx.render("public/registro-donacion-persona.hbs");
      return;
    }
    ctx.redirect("/");
  }

  public void registrarDonacion(Context ctx) {
    ctx.redirect("/");
  }

  public void mostrarUbicacionDonaciones(Context ctx) {
    String token = ctx.cookie("session");
    if (token == null || token.isBlank()) {
      ctx.render("public/mapa-donaciones.hbs");
      return;
    }
    try {
      UsuarioLogueado usuario = JwtRepositorio.Instance.obtenerUsuario(token);
      switch (usuario.getRol()) {
        case ADMIN -> ctx.render("admin/mapa-donaciones.hbs");
        case DONANTE -> ctx.render("donantes/mapa-donaciones.hbs");
        case BENEFICIARIO -> ctx.render("beneficiarios/donaciones-asignadas.hbs");
      }
    } catch (Exception e) {
      ctx.redirect("/");
    }
  }

  public void chequearAutorizacion(@NotNull Context context) {
  }
}
