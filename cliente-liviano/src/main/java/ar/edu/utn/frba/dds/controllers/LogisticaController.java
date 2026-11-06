package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.clients.LogisticaHttpClient;
import ar.edu.utn.frba.dds.dto.CamionDto;
import ar.edu.utn.frba.dds.dto.CamionRequest;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogisticaController {
  public void mostrarCamiones(Context ctx) {
    Map<String, Object> modelo = new HashMap<>();
    System.out.println(">>> Entró a mostrarCamiones");

    try {
      String token = ctx.cookie("session");

      List<CamionDto> camiones =
          new LogisticaHttpClient().listarCamiones(token);

      System.out.println(">>> Camiones obtenidos: " + camiones.size());

      modelo.put("camiones", camiones);
      modelo.put("logueado", true);
      ctx.render("admin/camiones.hbs", modelo);

    } catch (Exception e) {
      System.err.println("Error en mostrarCamiones:");
      e.printStackTrace();
      ctx.redirect("/");
    }
  }

  public void registrarCamion(@NotNull Context ctx) {
    String patente = ctx.formParam("patente");
    String capacidadVolumen = ctx.formParam("capacidadVolumen");
    String altura = ctx.formParam("altura");
    String capacidadCarga = ctx.formParam("capacidadCarga");
    String estado = ctx.formParam("estado");

    Map<String, Object> modelo = new HashMap<>();

    try {
      CamionRequest request = new CamionRequest();

      request.setPatente(patente);
      request.setCapacidadVolumen(Double.parseDouble(capacidadVolumen));
      request.setAltura(Double.parseDouble(altura));
      request.setCapacidadCarga(Double.parseDouble(capacidadCarga));
      request.setEstado(estado);

      HttpResponse<String> response = new LogisticaHttpClient()
          .registrarCamion(request);

      if (response.statusCode() == 201) {
        ctx.redirect("/admin/camiones?registro=exitoso");
        return;

      } else if (response.statusCode() == 409) {
        modelo.put("error", "Ya existe un camión registrado con esa patente.");

      } else if (response.statusCode() == 400) {
        modelo.put("error", "Los datos ingresados para el camión no son válidos.");

      } else {
        modelo.put("error", "No se pudo completar el registro del camión.");
      }

    } catch (NumberFormatException e) {
      modelo.put("error", "La capacidad, altura y volumen deben ser valores numéricos.");

    } catch (Exception e) {
      modelo.put("error", "No fue posible comunicarse con el servidor.");
    }

    ctx.render("admin/nuevo-camion.hbs", modelo);
  }
}
