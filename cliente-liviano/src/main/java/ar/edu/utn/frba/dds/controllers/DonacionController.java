package ar.edu.utn.frba.dds.controllers;

import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class DonacionController {
  public void registrarDonacion(@NotNull Context context) {
  }

  public void mostrarDonaciones(Context ctx) {
    ctx.render("public/donaciones.hbs");
  }

  public void chequearAutorizacion(@NotNull Context context) {
  }
}
