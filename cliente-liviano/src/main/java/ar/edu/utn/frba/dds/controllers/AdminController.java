package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.clients.HttpClient.DonacionesHttpClient;
import ar.edu.utn.frba.dds.dto.DonacionDto;
import ar.edu.utn.frba.dds.dto.EntidadDTO;
import ar.edu.utn.frba.dds.dto.RankingDTO;
import ar.edu.utn.frba.dds.dto.SegmentadaDTO;
import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import ar.edu.utn.frba.dds.vistas.Formato;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.jetbrains.annotations.NotNull;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminController {

  private final DonacionesHttpClient donaciones = new DonacionesHttpClient();
  private final ObjectMapper objectMapper = new ObjectMapper();

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

  public void mostrarDonaciones(Context ctx) {
    String token = ctx.cookie("session");
    Map<String, Object> modelo = new HashMap<>();
    modelo.put("asignada", ctx.queryParam("asignada"));
    modelo.put("errorAsignacion","asignacion".equals(ctx.queryParam("error")));

    String vencidas = ctx.queryParam("vencidas");
    if (vencidas != null && !vencidas.equals("0")) {
      modelo.put("vencidas", vencidas);
    }

    String noVencidas = ctx.queryParam("noVencidas");
    if(noVencidas != null && !noVencidas.equals("0")) {
      modelo.put("noVencidas", noVencidas);
    }
    modelo.put("errorSinSeleccion","sinSeleccion".equals(ctx.queryParam("error")));

    try{
      List<DonacionDto> todas = donaciones.listarDonaciones(token);

      HttpResponse<String> resposeEntidades = donaciones.listarEntidades(token);
      Map<Long,String> nombresEntidades = new HashMap<>();

      if(resposeEntidades.statusCode() == 200){
        List<EntidadDTO> entidades = objectMapper.readValue(resposeEntidades.body(),
            new TypeReference<List<EntidadDTO>>() {});

        for(EntidadDTO entidad : entidades){
          nombresEntidades.put(entidad.getId(),entidad.getRazonSocial());
        }

      }else {
        modelo.put("error", "No se pudieron cargar las entidades (codigo " + resposeEntidades.statusCode() + ")");
      }

      List<Map<String,Object>> filas = new ArrayList<>();
      List<Map<String,Object>> pendientes = new ArrayList<>();

      for(DonacionDto donacion : todas){
        for (SegmentadaDTO segmentada : donacion.getSegmentadas()){

          Map<String,Object> fila = fila(donacion,segmentada,nombresEntidades);

          filas.add(fila);

          if("En depósito".equals(segmentada.getEstado())){
            pendientes.add(fila);
          }

        }
      }

      modelo.put("donaciones", filas);
      modelo.put("donacionesPendientes", pendientes);

      String seleccionada = ctx.queryParam("seleccionada");

      if(seleccionada != null){
        cargarRanking(modelo, Long.parseLong(seleccionada),pendientes,token);
      }

    }catch(Exception e){

      modelo.put("error","No se pudo conectar con el servicio de Donaciones.");
    }

    ctx.render("admin/donaciones.hbs", modelo);
  }

  public void asignarDonacion(Context ctx) {
    Long segmentadaId = Long.parseLong(ctx.pathParam("id"));

    try {
      Long entidadId = Long.parseLong(ctx.formParam("entidadId"));
      HttpResponse<String> respuesta = donaciones.asignar(segmentadaId, entidadId, ctx.cookie("session"));
      if (respuesta.statusCode() / 100 == 2) {
        ctx.redirect("/admin/donaciones?asignada=" + segmentadaId);
        return;
      }
    } catch (Exception e) {
      System.err.println("Error asignando la donación " + segmentadaId + ": " + e.getMessage());
    }
    ctx.redirect("/admin/donaciones?seleccionada=" + segmentadaId + "&error=asignacion");
  }

  public void marcarVencidas(Context ctx) {
    List<String> ids = ctx.formParams("donaciones");

    if(ids.isEmpty()){
      ctx.redirect("/admin/donaciones?error=sinSeleccion");
      return;
    }

    String token = ctx.cookie("session");
    int vencidas = 0;
    int noVencidas = 0;

    for (String id : ids) {
      try{
        HttpResponse<String> respuesta = donaciones.vencer(Long.parseLong(id),token);
        if(respuesta.statusCode() / 100==2){
          vencidas++;
        }else{
          noVencidas++;
        }
      }catch (Exception e){
        System.err.println("Error venciendo la donación " + id + ": " + e.getMessage());
        noVencidas++;
      }
    }

    ctx.redirect("/admin/donaciones?vencidas=" + vencidas + "&noVencidas=" + noVencidas);


  }


  private void cargarRanking(Map<String, Object> modelo, Long segmentadaId,
                             List<Map<String, Object>> pendientes, String token) throws Exception {

    pendientes.stream()
        .filter(f -> segmentadaId.equals(f.get("id")))
        .findFirst()
        .ifPresent(f -> modelo.put("seleccion", f));

    HttpResponse<String> responseRanking = donaciones.obtenerRanking(segmentadaId,token);

    if(responseRanking.statusCode() == 404){
      return;
    }
    if(responseRanking.statusCode() != 200){
      modelo.put("error", "No se pudo obtener el ranking (codigo " + responseRanking.statusCode() + ").");
      return;
    }

    RankingDTO ranking = objectMapper.readValue(responseRanking.body(), RankingDTO.class);

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



}


