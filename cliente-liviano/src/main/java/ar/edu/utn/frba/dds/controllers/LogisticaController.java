package ar.edu.utn.frba.dds.controllers;

import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class LogisticaController {
  public void mostrarCamiones(Context ctx) {
    ctx.render("admin/camiones.hbs");
  }
}
