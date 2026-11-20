package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.clients.GrpcClientImplementation.GrpcClientGestionDonacionesImplementation;
import ar.edu.utn.frba.dds.dto.SegmentadaPanelDTO;
import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import ar.edu.utn.frba.dds.vistas.Filtros;
import ar.edu.utn.frba.dds.vistas.Formato;
import io.javalin.http.Context;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeneficiarioController {

  private final GrpcClientGestionDonacionesImplementation gestionDonaciones = new GrpcClientGestionDonacionesImplementation();

  //pantalla 9
  public void donacionesAsignadas(Context ctx){
    UsuarioLogueado usuario = ctx.attribute("usuarioLogueado");

    //el rol lo valida el RolMiddleware; este if queda porque su redirect no corta el handler
    if(usuario == null){
      ctx.redirect("/login");
      return;
    }

    String estado = Filtros.valor(ctx, "estado");
    String categoria = Filtros.valor(ctx, "categoria");

    Map<String, Object> modelo = new HashMap<>();

    modelo.put("hayFiltros",estado != null || categoria != null);

    try {

      List<SegmentadaPanelDTO> todas = gestionDonaciones.listarDonacionesAsignadas(usuario.getId());

      List<Map<String, Object>> filas = todas.stream()
          .filter(s->estado == null || estado.equals(s.getEstado()))
          .filter(s -> categoria == null || categoria.equals(s.getCategoria()))
          .map(this::fila)
          .toList();

      modelo.put("filas", filas);
      modelo.put("hayDonaciones", !todas.isEmpty());
      modelo.put("estados", Filtros.opciones(todas,SegmentadaPanelDTO::getEstado, estado));
      modelo.put("categorias", Filtros.opciones(todas,SegmentadaPanelDTO::getCategoria, categoria));

    }catch (Exception e){
      System.err.println("Error listando las donaciones asignadas a la entidad " + usuario.getId() + ": " + e.getMessage());
      modelo.put("error", "No pudimos cargar las donaciones asignadas. Probá de nuevo en unos minutos.");
    }

    ctx.render("beneficiarios/donaciones-asignadas.hbs", modelo);
  }

  //sin columna de entidad: todas son de esta entidad
  private Map<String, Object> fila(SegmentadaPanelDTO s){
    Map<String, Object> f = new HashMap<>();
    f.put("donacionId", s.getDonacionId());
    f.put("fecha", Formato.fecha(s.getFechaRecepcion()));
    f.put("categoria", Formato.texto(s.getCategoria()));
    f.put("subcategoria", Formato.texto(s.getSubcategoria()));
    f.put("cantidad", Formato.cantidad(new BigDecimal(s.getCantidad()), s.getUnidad()));
    f.put("estado", s.getEstado());
    f.put("badge", Formato.badge(s.getEstado()));
    return f;
  }



}
