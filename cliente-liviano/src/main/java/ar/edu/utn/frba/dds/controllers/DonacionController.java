package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import ar.edu.utn.frba.dds.repositorios.JwtRepositorio;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class DonacionController {
  public void registrarDonacion(@NotNull Context context) {
  }

  public void mostrarDonaciones(Context ctx) {
    String token = ctx.cookie("session");
    if (token == null || token.isBlank()) {
      ctx.redirect("/");
      return;
    }
    try {
      UsuarioLogueado usuario = JwtRepositorio.Instance.obtenerUsuario(token);
      switch (usuario.getRol()) {
        case ADMIN -> ctx.render("admin/donaciones.hbs");
        case DONANTE -> ctx.render("donantes/donaciones.hbs");
        case BENEFICIARIO -> ctx.render("beneficiarios/donaciones-asignadas.hbs");
      }
    } catch (Exception e) {
      ctx.redirect("/");
    }
  }

  public void chequearAutorizacion(@NotNull Context context) {
  }
}
