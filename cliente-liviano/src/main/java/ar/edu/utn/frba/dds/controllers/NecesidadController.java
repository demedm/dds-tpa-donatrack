package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.clients.GrpcClientImplementation.GrpcClientNecesidadImplementation;
import ar.edu.utn.frba.dds.dto.NecesidadDto;
import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import io.grpc.StatusRuntimeException;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NecesidadController {

  private final GrpcClientNecesidadImplementation grpcNecesidadClient = new GrpcClientNecesidadImplementation();

  private Map<String, Object> crearModeloBase(UsuarioLogueado usuarioLogueado) {
    Map<String, Object> model = new HashMap<>();
    model.put("logueado", usuarioLogueado != null);
    model.put("esBeneficiario", usuarioLogueado != null && "BENEFICIARIO".equalsIgnoreCase(String.valueOf(usuarioLogueado.getRol())));
    return model;
  }

  public void showNecesidades(Context ctx) {
    UsuarioLogueado usuarioLogueado = ctx.attribute("usuarioLogueado");
    if (usuarioLogueado == null) {
      ctx.redirect("/login");
      return;
    }
    Long idUsuario = null;
    if (usuarioLogueado != null) {
      idUsuario = usuarioLogueado.getId();
    } else {
      String rawEntidad = ctx.queryParam("entidadId");
      if (rawEntidad != null && !rawEntidad.isBlank()) {
        idUsuario = Long.parseLong(rawEntidad);
      }
    }

    List<NecesidadDto> necesidades = (idUsuario != null)
            ? grpcNecesidadClient.listarPorEntidad(idUsuario)
            : Collections.emptyList();

    Map<String, Object> model = crearModeloBase(usuarioLogueado);
    model.put("idUsuario", idUsuario);
    model.put("entidadId", idUsuario);
    model.put("titulo", "Mis Necesidades");
    model.put("necesidades", necesidades);

    ctx.render("beneficiarios/necesidades/necesidades.hbs", model);
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

    Long nuevaNecesidadId = grpcNecesidadClient.crearNecesidad(entidadId, descripcion, tipo, diasRecurrencia);

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
    Double cantidadRequerida = (cantidadString != null && !cantidadString.isBlank())
            ? Double.parseDouble(cantidadString)
            : 0.0;

    grpcNecesidadClient.agregarPeticion(necesidadId, subclase, cantidadRequerida);

    ctx.redirect("/necesidades/" + necesidadId);
  }

  public void showNecesidad(Context ctx) {
    UsuarioLogueado usuarioLogueado = ctx.attribute("usuarioLogueado");
    if (usuarioLogueado == null) {
      ctx.redirect("/login");
      return;
    }
    Long id = Long.parseLong(ctx.pathParam("id"));
    NecesidadDto necesidad;
    try {
      necesidad = grpcNecesidadClient.obtenerDetalle(id);
    } catch (StatusRuntimeException e) {
    if (e.getStatus().getCode() == io.grpc.Status.Code.NOT_FOUND) {
      throw new NotFoundResponse("La necesidad solicitada no existe."); // 404
    }
    throw e;
  }

    if (necesidad == null) {
    throw new io.javalin.http.NotFoundResponse("La necesidad solicitada no existe.");
  }

    if (!usuarioLogueado.getId().equals(necesidad.getEntidadId())) {
    throw new io.javalin.http.ForbiddenResponse("No tenés permiso para ver esta necesidad."); // 403
  }
    Map<String, Object> model = crearModeloBase(usuarioLogueado);
    model.put("id", id);
    model.put("necesidad", necesidad);
    model.put("cloudinaryCloudName", System.getenv("CLOUDINARY_CLOUD_NAME"));
    model.put("cloudinaryPreset", System.getenv("CLOUDINARY_UPLOAD_PRESET"));

    ctx.render("beneficiarios/necesidades/necesidadDetalle.hbs", model);
  }

  public void subirAdjunto(Context ctx) {
    UsuarioLogueado usuarioLogueado = ctx.attribute("usuarioLogueado");
    if (usuarioLogueado == null) {
      ctx.redirect("/login");
      return;
    }

    Long id = Long.parseLong(ctx.pathParam("id"));
    NecesidadDto necesidad = grpcNecesidadClient.obtenerDetalle(id);
    if (necesidad == null) {
      throw new NotFoundResponse("La necesidad solicitada no existe.");
    }
    if (!usuarioLogueado.getId().equals(necesidad.getEntidadId())) {
      ctx.redirect("/necesidades");
      return;
    }

    String url = ctx.formParam("url");
    if (url != null && !url.isBlank()) {
      System.out.println("URL recibida: " + url);
      grpcNecesidadClient.agregarAdjunto(id, url.trim());
    }

    ctx.redirect("/necesidades/" + id);
  }
}