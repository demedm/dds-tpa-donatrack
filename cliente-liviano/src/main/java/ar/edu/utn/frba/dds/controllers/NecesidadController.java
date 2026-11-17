package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.clients.GrpcClientNecesidadImplementation;
import ar.edu.utn.frba.dds.dto.NecesidadDto;
import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import io.javalin.http.Context;

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
    Long id = Long.parseLong(ctx.pathParam("id"));
    NecesidadDto necesidad = grpcNecesidadClient.obtenerDetalle(id);

    Map<String, Object> model = crearModeloBase(usuarioLogueado);
    model.put("id", id);
    model.put("necesidad", necesidad);

    ctx.render("beneficiarios/necesidades/necesidadDetalle.hbs", model);
  }

  public void subirAdjunto(Context ctx) {
    Long id = Long.parseLong(ctx.pathParam("id"));
    ctx.redirect("/necesidades/" + id);
  }
}