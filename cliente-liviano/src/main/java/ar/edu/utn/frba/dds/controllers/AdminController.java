package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.clients.DonacionesHttpClient;
import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.jetbrains.annotations.NotNull;

import java.net.http.HttpResponse;
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

  public void importarDonantesDesdeCsv(@NotNull Context ctx) {
    Map<String, Object> modelo = new HashMap<>();
    try {
      UploadedFile archivo = ctx.uploadedFile("archivo");

      if (archivo == null) {
        ctx.status(400);
        modelo.put("error", "No se recibió ningún archivo");
        return;
      }

      HttpResponse<String> response = new DonacionesHttpClient().importarDonantes(archivo);
      ctx.status(response.statusCode());
      modelo.put("exito", "Donantes cargados con éxito!");
      // ctx.result(response.body());
    } catch (Exception e) {
      ctx.status(500);
      modelo.put("error", "Error al importar donantes");
    }
    ctx.render("/admin/panel", modelo);
  }
}
