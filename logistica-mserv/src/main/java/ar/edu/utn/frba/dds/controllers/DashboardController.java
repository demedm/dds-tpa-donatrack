package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.repositories.CamionRepositorio;import io.javalin.http.Context;import java.util.Map;

public class DashboardController {
  public void index(Context ctx) {

  }

  // muestra la posición actual de los camiones y su avance sobre la ruta asignada en tiempo real.
  public void listarCamiones(Context ctx) {
    ctx.render("listado.hbs", Map.of("camiones",
        CamionRepositorio.Instance.getFlota()));
  }

}