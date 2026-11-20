package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.clients.GrpcClientImplementation.GrpcClientGestionDonacionesImplementation;
import ar.edu.utn.frba.dds.dto.SegmentadaPanelDTO;
import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import ar.edu.utn.frba.dds.session.Rol;
import ar.edu.utn.frba.dds.vistas.Filtros;
import ar.edu.utn.frba.dds.vistas.Formato;
import io.javalin.http.Context;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static ar.edu.utn.frba.dds.vistas.Filtros.opciones;


public class DonanteController {
  private final GrpcClientGestionDonacionesImplementation gestionDonaciones = new GrpcClientGestionDonacionesImplementation();

  //pantalla 5
  public void misDonaciones(Context ctx){
    UsuarioLogueado usuario = ctx.attribute("usuarioLogueado");

    //borrar estos if cuando se active el app.before

    if (usuario == null){
      ctx.redirect("/login");
      return;
    }

    if(usuario.getRol() != Rol.DONANTE){
      ctx.status(403).result("Esta página es solo para personas donantes.");
      return;
    }

    String estado = Filtros.valor(ctx, "estado");
    String categoria = Filtros.valor(ctx, "categoria");
    String subcategoria = Filtros.valor(ctx, "subcategoria");

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("logueado", true);
    modelo.put("esDonante", true);
    modelo.put("hayFiltros", estado !=null || categoria !=null || subcategoria !=null );

    try {
      List<SegmentadaPanelDTO> todas = gestionDonaciones.listarDonacionesDeDonante(usuario.getId());

      List<Map<String, Object>> filas = todas.stream()
          .filter(s -> estado == null || estado.equals(s.getEstado()))
          .filter(s -> categoria == null || categoria.equals(s.getCategoria()))
          .filter(s -> subcategoria == null || subcategoria.equals(s.getSubcategoria()))
          .map(this::fila)
          .toList();

      modelo.put("filas", filas);
      modelo.put("hayDonaciones", !todas.isEmpty());
      modelo.put("estados", Filtros.opciones(todas, SegmentadaPanelDTO::getEstado, estado));
      modelo.put("categorias", Filtros.opciones(todas, SegmentadaPanelDTO::getCategoria, categoria));
      modelo.put("subcategorias", Filtros.opciones(todas, SegmentadaPanelDTO::getSubcategoria, subcategoria));

    } catch (Exception e){
      System.err.println("Error listando las donaciones del donante " + usuario.getId() + ": " + e.getMessage());
      modelo.put("error", "No pudimos cargar tus donaciones. Probá de nuevo en unos minutos.");
    }

    ctx.render("donantes/donaciones.hbs",modelo);

  }


  private Map<String,Object> fila(SegmentadaPanelDTO s){
    Map<String,Object> f = new HashMap<>();
    f.put("donacionId",s.getDonacionId());
    f.put("fecha", Formato.fecha(s.getFechaRecepcion()));
    f.put("categoria", Formato.texto(s.getCategoria()));
    f.put("subcategoria", Formato.texto(s.getSubcategoria()));
    f.put("cantidad", Formato.cantidad(new BigDecimal(s.getCantidad()),s.getUnidad()));
    f.put("estado", s.getEstado());
    f.put("badge", Formato.badge(s.getEstado()));
    f.put("entidad",textoEntidad(s));
    return f;

  }

  private String textoEntidad(SegmentadaPanelDTO s){
    if (!s.getEntidadAsignada().isEmpty()){
      return s.getEntidadAsignada();
    }
    return "Vencida".equals(s.getEstado()) ? "No se asignó (venció)" : "Todavía sin asignar";
  }


}
