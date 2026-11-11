package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NecesidadController {
  public void showNecesidades(Context ctx) {

    UsuarioLogueado usuarioLogueado = ctx.attribute("usuarioLogueado");

    Long idUsuario = null;
    if (usuarioLogueado != null) {
      idUsuario = usuarioLogueado.getId();
    } else {
      String rawEntidad = ctx.queryParam("entidadId");
      if (rawEntidad != null && !rawEntidad.isBlank()) {
        idUsuario = Long.parseLong(rawEntidad);
      }
    }


    List<Object> necesidades = new ArrayList<>();
    if (idUsuario != null) {
      try {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:9001/necesidades?entidadId=" + idUsuario))
            .header("Accept", "application/json")
            .GET()
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
          ObjectMapper mapper = new ObjectMapper();
          necesidades = mapper.readValue(response.body(), new TypeReference<List<Object>>() {});
        } else {
          System.err.println("Error al obtener necesidades de Donaciones MS: " + response.body());
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }


    Map<String, Object> model = new HashMap<>();
    model.put("idUsuario", idUsuario);
    model.put("entidadId", idUsuario);
    model.put("titulo", "Mis Necesidades");
    model.put("necesidades", necesidades); // <-- Se agrega la lista para la vista

    ctx.render("beneficiarios/necesidades.hbs", model);
  }

  public void crear(Context ctx) {
    UsuarioLogueado usuarioLogueado = ctx.attribute("usuarioLogueado");

    if (usuarioLogueado == null) {
      ctx.redirect("/login");
      return;
    }

    Long entidadId = usuarioLogueado.getId();
    String descripcion = ctx.formParam("descripcion");
    String tipo = ctx.formParam("tipo");
    String diasStr = ctx.formParam("diasRecurrencia");

    Integer diasRecurrencia = (diasStr != null && !diasStr.isBlank()) ? Integer.parseInt(diasStr) : null;

    String jsonBody = String.format(
            "{\"entidadId\": %d, \"descripcion\": \"%s\", \"tipo\": \"%s\"%s}",
            entidadId,
            descripcion,
            tipo,
            diasRecurrencia != null ? ", \"diasRecurrencia\": " + diasRecurrencia : ""
    );

    Long nuevaNecesidadId = null;

    try {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create("http://localhost:9001/necesidades/"))
              .header("Content-Type", "application/json")
              .header("Accept", "application/json")
              .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
              .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 201 || response.statusCode() == 200) {
        String responseBody = response.body();
        if (responseBody.contains("\"id\":")) {
          String[] partes = responseBody.split("\"id\":");
          String idStr = partes[1].split(",")[0].replaceAll("[^0-9]", "");
          nuevaNecesidadId = Long.parseLong(idStr);
        }
      } else {
        System.err.println("Error al crear necesidad en Donaciones MS: " + response.body());
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    if (nuevaNecesidadId != null) {
      ctx.redirect("/necesidades/" + nuevaNecesidadId);
    } else {
      ctx.redirect("/necesidades");
    }
  }


  public void agregarPeticion(Context ctx) {
    UsuarioLogueado usuarioLogueado = ctx.attribute("usuarioLogueado");
    if (usuarioLogueado == null) {
      ctx.redirect("/login");
      return;
    }

    Long necesidadId = Long.parseLong(ctx.pathParam("id"));
    String subclase = ctx.formParam("subclase");
    String cantidadString = ctx.formParam("cantidadRequerida");
    Integer cantidadRequerida = (cantidadString != null && !cantidadString.isBlank()) ? Integer.parseInt(cantidadString) : 0;

    String jsonBody = String.format(
            "{\"subclase\": \"%s\", \"cantidadRequerida\": %d}",
            subclase,
            cantidadRequerida
    );

    try {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create("http://localhost:9001/necesidades/" + necesidadId + "/peticiones/"))
              .header("Content-Type", "application/json")
              .header("Accept", "application/json")
              .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
              .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 201 || response.statusCode() == 200) {
        System.out.println("Petición agregada con éxito en Donaciones MS");
      } else {
        System.err.println("Error al agregar petición en Donaciones MS: " + response.body());
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    ctx.redirect("/necesidades/" + necesidadId);
  }


  public void showNecesidad(Context ctx) {
    Long id = Long.parseLong(ctx.pathParam("id"));
    Map<String, Object> model = new HashMap<>();
    model.put("id", id);
    ctx.render("necesidadDetalle.hbs", model);
  }


  public void subirAdjunto(Context ctx) {
    Long id = Long.parseLong(ctx.pathParam("id"));
    ctx.redirect("/necesidades/" + id);
  }


}