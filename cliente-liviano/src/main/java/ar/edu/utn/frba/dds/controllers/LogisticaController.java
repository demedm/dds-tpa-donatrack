package ar.edu.utn.frba.dds.controllers;

import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class LogisticaController {
  public void mostrarCamiones(Context ctx) {
    ctx.render("admin/camiones.hbs");
  }
  public void entregasEnCurso(Context ctx) {
    var idUsuario = Long.parseLong(ctx.pathParam("idUsuario"));
    ctx.render("entregas-en-curso.hbs", Map.of("idUsuario", idUsuario));
  }
}
