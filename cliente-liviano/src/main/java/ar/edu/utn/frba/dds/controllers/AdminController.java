package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.clients.GrpcClientImplementation.GrpcClientNecesidadImplementation;
import ar.edu.utn.frba.dds.clients.GrpcClientImplementation.GrpcClientPlaniImplementation;
import ar.edu.utn.frba.dds.clients.GrpcClientImplementation.GrpcClientGestionDonacionesImplementation;
import ar.edu.utn.frba.dds.clients.HttpClient.DonacionesHttpClient;
import ar.edu.utn.frba.dds.dto.DonacionDto;
import ar.edu.utn.frba.dds.dto.EntidadDTO;
import ar.edu.utn.frba.dds.dto.NecesidadDto;
import ar.edu.utn.frba.dds.dto.PeticionDto;
import ar.edu.utn.frba.dds.dto.RankingDTO;
import ar.edu.utn.frba.dds.dto.SegmentadaDTO;
import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import ar.edu.utn.frba.dds.vistas.Formato;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planificacionServiceGrpc.grpc.DonacionAPlanificar;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AdminController {

  private final DonacionesHttpClient donaciones = new DonacionesHttpClient();
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final GrpcClientGestionDonacionesImplementation gestionDonaciones = new GrpcClientGestionDonacionesImplementation();
  private static final GrpcClientPlaniImplementation clientPlanificacion = new GrpcClientPlaniImplementation();
  private final GrpcClientNecesidadImplementation clientNecesidades =
      new GrpcClientNecesidadImplementation();

  public static GrpcClientPlaniImplementation getClientPlanificacion() {
    return clientPlanificacion;
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
        ctx.render("admin/administracion.hbs", modelo);
        return;
      }
      String token = ctx.cookie("session");
      HttpResponse<String> response = donaciones.importarDonantes(archivo, token);
      ctx.status(response.statusCode());
      if (response.statusCode() >= 200 && response.statusCode() < 300) {
        modelo.put("exito", "Donantes cargados con éxito!");
      } else {
        modelo.put("error", "Error al importar: " + response.body());
      }
    } catch (Exception e) {
      ctx.status(500);
      modelo.put("error", "Error al importar donantes: " + e.getMessage());
    }
    ctx.render("admin/administracion.hbs", modelo);
  }

  public void mostrarDonaciones(Context ctx) {
    String token = ctx.cookie("session");
    Map<String, Object> modelo = new HashMap<>();
    modelo.put("registrada", ctx.queryParam("registrada"));
    modelo.put("asignada", ctx.queryParam("asignada"));
    modelo.put("errorAsignacion", "asignacion".equals(ctx.queryParam("error")));

    String vencidas = ctx.queryParam("vencidas");
    if (vencidas != null && !vencidas.equals("0")) {
      modelo.put("vencidas", vencidas);
    }

    String noVencidas = ctx.queryParam("noVencidas");
    if (noVencidas != null && !noVencidas.equals("0")) {
      modelo.put("noVencidas", noVencidas);
    }
    modelo.put("errorSinSeleccion", "sinSeleccion".equals(ctx.queryParam("error")));

    try {
      List<DonacionDto> todas = donaciones.listarDonaciones(token);

      Map<Long, String> nombresEntidades = new HashMap<>();

      try {
        for (EntidadDTO entidad : gestionDonaciones.listarEntidades()) {
          nombresEntidades.put(entidad.getId(), entidad.getRazonSocial());
        }
      } catch (Exception e) {
        modelo.put("error", "No se pudieron cargar las entidades: " + e.getMessage());
      }

      List<Map<String, Object>> filas = new ArrayList<>();
      List<Map<String, Object>> pendientes = new ArrayList<>();

      for (DonacionDto donacion : todas) {
        for (SegmentadaDTO segmentada : donacion.getSegmentadas()) {

          Map<String, Object> fila = fila(donacion, segmentada, nombresEntidades);

          filas.add(fila);

          if ("En depósito".equals(segmentada.getEstado())) {
            pendientes.add(fila);
          }

        }
      }

      modelo.put("donaciones", filas);
      modelo.put("donacionesPendientes", pendientes);

      String seleccionada = ctx.queryParam("seleccionada");

      if (seleccionada != null) {
        cargarRanking(modelo, Long.parseLong(seleccionada), pendientes);
      }

    } catch (Exception e) {

      modelo.put("error", "No se pudo conectar con el servicio de Donaciones.");
    }

    ctx.render("admin/donaciones.hbs", modelo);
  }

  // la usan el botón "Asignar" del ranking y la pantalla de asignación manual: la entidad viene en el formulario
  public void asignarDonacion(Context ctx) {
    Long segmentadaId = Long.parseLong(ctx.pathParam("id"));
    String token = ctx.cookie("session");

    try {
      Long entidadId = Long.parseLong(ctx.formParam("entidadId"));

      if (gestionDonaciones.asignarDonacionManualmente(segmentadaId, entidadId)) {
        boolean enviada = enviarALogistica(token, segmentadaId, entidadId);
        ctx.redirect("/admin/donaciones?asignada=" + segmentadaId +
            (enviada ? "" : "&logistica=error"));
        return;
      }

    } catch (Exception e) {
      System.err.println("Error asignando la donación " + segmentadaId + ": " + e.getMessage());
    }
    ctx.redirect("/admin/donaciones?seleccionada=" + segmentadaId + "&error=asignacion");
  }

  private boolean enviarALogistica(String token, Long segmentadaId, Long entidadId) {
    try {
      return clientPlanificacion.enviarDonacion(armarDonacion(token, segmentadaId, entidadId));
    } catch (Exception e) {
      System.err.println("Donación " + segmentadaId + " asignada pero no enviada a logística: "
          + e.getMessage());
      return false;
    }
  }

  private DonacionAPlanificar armarDonacion(String token, Long segmentadaId, Long entidadId) {
    for (DonacionDto donacion : donaciones.listarDonaciones(token)) {
      for (SegmentadaDTO s : donacion.getSegmentadas()) {
        if (!segmentadaId.equals(s.getId())) continue;                 // ASUMO getId()

        EntidadDTO entidad = gestionDonaciones.listarEntidades().stream()
            .filter(e -> entidadId.equals(e.getId()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Entidad inexistente: " + entidadId));

        return DonacionAPlanificar.newBuilder()
            .setDonacionId(segmentadaId)
            .setBeneficiarioId(entidadId)
            .setDireccion(entidad.getDireccion())
            .setFechaVencimiento(s.getVencimiento() == null
                ? "" : s.getVencimiento().toString())
            .setCantidad(s.getCantidad().doubleValue())
            .setUnidadMedida(String.valueOf(s.getUnidad()))
            .setUso(String.valueOf(s.getEstadoUso()))
            .build();
      }
    }
    throw new IllegalStateException("Segmentada inexistente: " + segmentadaId);
  }

  public void marcarVencidas(Context ctx) {
    List<String> ids = ctx.formParams("donaciones");

    if (ids.isEmpty()) {
      ctx.redirect("/admin/donaciones?error=sinSeleccion");
      return;
    }

    int vencidas = 0;
    int noVencidas = 0;

    for (String id : ids) {
      try {
        if (gestionDonaciones.marcarVencida(Long.parseLong(id))) {
          vencidas++;
        } else {
          noVencidas++;
        }
      } catch (Exception e) {
        System.err.println("Error venciendo la donación " + id + ": " + e.getMessage());
        noVencidas++;
      }
    }

    ctx.redirect("/admin/donaciones?vencidas=" + vencidas + "&noVencidas=" + noVencidas);


  }

  public void mostrarNuevaDonacion(Context ctx) {
    Map<String, Object> modelo = new HashMap<>();
    modelo.put("error", ctx.queryParam("error"));

    try {
      modelo.put("donantes", gestionDonaciones.listarDonantes());
    } catch (Exception e) {
      modelo.put("error", "No se pudieron cargar los donantes: " + e.getMessage());
    }

    ctx.render("admin/nueva-donacion.hbs", modelo);

  }

  public void registrarDonacion(Context ctx) {
    Long donanteId;

    try {
      donanteId = Long.parseLong(ctx.formParam("donanteId"));
    } catch (Exception e) {
      volverAlFormulario(ctx, "Elegir el donante de la donacion");
      return;
    }

    Map<Integer, Map<String, String>> articulos = new TreeMap<>();

    for (Map.Entry<String, List<String>> campo : ctx.formParamMap().entrySet()) {

      String nombre = campo.getKey();

      if (!nombre.startsWith("articulos[") || !nombre.endsWith("]") || campo.getValue().isEmpty()) {
        continue;
      }

      String resto = nombre.substring("articulos[".length(), nombre.length() - 1);

      int separador = resto.indexOf("][");
      if (separador == -1) {
        continue;
      }

      int indice;

      try {
        indice = Integer.parseInt(resto.substring(0, separador));
      } catch (NumberFormatException e) {
        continue;
      }

      String nombreCampo = resto.substring(separador + 2);

      articulos.computeIfAbsent(indice, i -> new HashMap<>())
          .put(nombreCampo, campo.getValue().get(0));

      }

      if (articulos.isEmpty()) {
        volverAlFormulario(ctx, "Carga al menos un articulo");
        return;
      }

      Long donacionId;
      try {
        donacionId = gestionDonaciones.crearDonacion(
            donanteId, ctx.formParam("descripcionGeneral"), new ArrayList<>(articulos.values()));

      } catch (Exception e) {
        System.err.println("Error registrando la donacion: " + e.getMessage());
        volverAlFormulario(ctx, e.getMessage());
        return;
      }


      ctx.redirect("/admin/donaciones?registrada=" + donacionId);


  }

  private void volverAlFormulario(Context ctx, String mensaje) {
    ctx.redirect("/admin/donaciones/nueva?error=" + URLEncoder.encode(mensaje, StandardCharsets.UTF_8));
  }



  private void cargarRanking(Map<String, Object> modelo, Long segmentadaId,
                             List<Map<String, Object>> pendientes) throws Exception {

    pendientes.stream()
        .filter(f -> segmentadaId.equals(f.get("id")))
        .findFirst()
        .ifPresent(f -> modelo.put("seleccion", f));


    RankingDTO ranking;

    try{
      ranking = gestionDonaciones.obtenerRanking(segmentadaId);
    }catch(Exception e){
      modelo.put("error","No se pudo obtener ranking: " + e.getMessage());
      return;
    }

    if(ranking == null){
      return;
    }

    modelo.put("resultados", conPosicion(ranking.getPropuestas()));
    modelo.put("huboCoincidencias", ranking.isHuboCoincidencias());



    if(!ranking.isHuboCoincidencias()){
      List<Map<String,Object>> algoritmos = new ArrayList<>();
      int i = 0;

      for(Map.Entry<String,List<EntidadDTO>> entrada: ranking.getPorAlgoritmo().entrySet()){
        Map<String,Object> algoritmo = new HashMap<>();
        algoritmo.put("nombre",entrada.getKey());
        algoritmo.put("panel", "panel-alg-" + i);
        algoritmo.put("entidades", conPosicion(entrada.getValue()));
        algoritmos.add(algoritmo);
        i++;
      }

      modelo.put("algoritmos", algoritmos);

    }

  }

  private List<Map<String, Object>> conPosicion(List<EntidadDTO> entidades) {
    List<Map<String, Object>> lista = new ArrayList<>();
    for (int i = 0; i < entidades.size(); i++) {
      EntidadDTO e = entidades.get(i);
      Map<String, Object> m = new HashMap<>();
      m.put("posicion", i + 1);
      m.put("id", e.getId());
      m.put("razonSocial", e.getRazonSocial());
      m.put("tipo", Formato.texto(e.getTipo()));
      m.put("direccion", e.getDireccion());
      lista.add(m);
    }
    return lista;
  }

  private Map<String, Object> fila(DonacionDto donacion, SegmentadaDTO s, Map<Long, String> nombresEntidades) {
    Map<String, Object> f = new HashMap<>();
    f.put("id", s.getId());
    f.put("descripcion", donacion.getDescripcionGeneral());
    f.put("bien", s.getSubcategoria());
    f.put("categoria", Formato.texto(s.getCategoria()));
    f.put("cantidad", Formato.cantidad(s.getCantidad(), s.getUnidad()));
    f.put("vencimiento", Formato.fecha(s.getVencimiento()));
    f.put("uso", Formato.texto(s.getEstadoUso()));
    f.put("donante", donacion.getDonanteNombre());
    f.put("entidad", s.getEntidadAsignadaId() == null ? "—"
        : nombresEntidades.getOrDefault(s.getEntidadAsignadaId(), "—"));
    f.put("estado", s.getEstado());
    f.put("badge", Formato.badge(s.getEstado()));
    f.put("vencible", "En depósito".equals(s.getEstado()));
    return f;
  }


  public void mostrarEntidadesParaAsignacion(Context ctx) {
    Long segmentadaId = Long.parseLong(ctx.pathParam("id"));
    String token = ctx.cookie("session");

    Map<String, Object> modelo = new HashMap<>();

    try {
      // 1. Buscamos la donación segmentada
      List<DonacionDto> todas = donaciones.listarDonaciones(token);

      Map<String, Object> donacionSeleccionada = null;

      for (DonacionDto donacion : todas) {
        for (SegmentadaDTO s : donacion.getSegmentadas()) {

          if (segmentadaId.equals(s.getId())) {

            donacionSeleccionada = new HashMap<>();

            donacionSeleccionada.put("id", s.getId());
            donacionSeleccionada.put("descripcion", donacion.getDescripcionGeneral());
            donacionSeleccionada.put("bien", s.getSubcategoria());
            donacionSeleccionada.put("categoria", Formato.texto(s.getCategoria()));
            donacionSeleccionada.put("cantidad", Formato.cantidad(s.getCantidad(), s.getUnidad()));
            donacionSeleccionada.put("vencimiento", Formato.fecha(s.getVencimiento()));

            break;
          }
        }

        if (donacionSeleccionada != null) {
          break;
        }
      }

      if (donacionSeleccionada == null) {
        ctx.redirect("/admin/donaciones?error=donacionInexistente");
        return;
      }

      modelo.put("donacion", donacionSeleccionada);

      // 2. Obtenemos las entidades
      List<EntidadDTO> entidadesDTO = gestionDonaciones.listarEntidades();

      List<Map<String, Object>> entidades = new ArrayList<>();

      for (EntidadDTO entidad : entidadesDTO) {
        Map<String, Object> e = new HashMap<>();

        e.put("id", entidad.getId());
        e.put("razonSocial", entidad.getRazonSocial());
        e.put("tipo", Formato.texto(entidad.getTipo()));
        e.put("direccion", entidad.getDireccion());
        e.put("telefono", entidad.getTelefono());

        // 3. Consultamos las necesidades de esta entidad
        List<NecesidadDto> necesidadesDTO =
            clientNecesidades.listarPorEntidad(entidad.getId());

        List<Map<String, Object>> necesidades = new ArrayList<>();

        for (NecesidadDto necesidad : necesidadesDTO) {
          Map<String, Object> n = new HashMap<>();

          n.put("id", necesidad.getId());
          n.put("descripcion", necesidad.getDescripcion());
          n.put("tipo", necesidad.getTipo());
          n.put("estado", necesidad.getEstado());
          n.put("proximoVencimiento", necesidad.getProximoVencimiento());
          n.put("vencida", necesidad.isVencida());

          // Peticiones asociadas a la necesidad
          List<Map<String, Object>> peticiones = new ArrayList<>();

          for (PeticionDto peticion : necesidad.getPeticiones()) {
            Map<String, Object> p = new HashMap<>();

            p.put("subclase", peticion.getSubclase());
            p.put("cantidadRequerida", peticion.getCantidadRequerida());
            p.put("cantidadRecibida", peticion.getCantidadRecibida());
            p.put("cantidadComprometida", peticion.getCantidadComprometida());

            peticiones.add(p);
          }

          n.put("peticiones", peticiones);
          necesidades.add(n);
        }

        e.put("necesidades", necesidades);

        entidades.add(e);
      }

      modelo.put("entidades", entidades);

      // 3. Renderizamos la vista
      ctx.render("admin/asignar-entidad.hbs", modelo);

    } catch (Exception e) {
      System.err.println("Error cargando entidades para asignación: " + e.getMessage());
      ctx.redirect("/admin/donaciones?error=entidades");
    }
  }

  public void asignarDonacionManualmente(@NotNull Context ctx) {
    Long segmentadaId = Long.parseLong(ctx.pathParam("id"));
    String token = ctx.cookie("session");

    try {
      Long entidadId = Long.parseLong(ctx.pathParam("entidadId"));

      if (gestionDonaciones.asignarDonacionManualmente(segmentadaId, entidadId)) {
        boolean enviada = enviarALogistica(token, segmentadaId, entidadId);
        ctx.redirect("/admin/donaciones?asignada=" + segmentadaId +
            (enviada ? "" : "&logistica=error"));
        return;
      }

    } catch (Exception e) {
      System.err.println("Error asignando la donación " + segmentadaId + ": " + e.getMessage());
    }
    ctx.redirect("/admin/donaciones?seleccionada=" + segmentadaId + "&error=asignacion");
  }
}


