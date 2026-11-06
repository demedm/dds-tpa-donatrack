package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.clients.LogisticaHttpClient;
import ar.edu.utn.frba.dds.dto.CamionDto;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class LogisticaController {
  public void mostrarCamiones(Context ctx) {
    System.out.println(">>> Entró a mostrarCamiones");

    try {
      String token = ctx.cookie("session");

      List<CamionDto> camiones =
          new LogisticaHttpClient().listarCamiones(token);

      System.out.println(">>> Camiones obtenidos: " + camiones.size());

      ctx.render("admin/camiones.hbs", Map.of("camiones", camiones));

    } catch (Exception e) {
      System.err.println("Error en mostrarCamiones:");
      e.printStackTrace();
      ctx.redirect("/");
    }
  }
}
