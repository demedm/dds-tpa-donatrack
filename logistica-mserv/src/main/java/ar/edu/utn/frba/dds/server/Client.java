package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.EstadoEntrega;import ar.edu.utn.frba.dds.scripts.dto.CambioEstadoDTO;
import ar.edu.utn.frba.dds.scripts.dto.CamionDTO;
import ar.edu.utn.frba.dds.scripts.dto.ConfirmationPlanificacionDTO;import ar.edu.utn.frba.dds.scripts.dto.RequestPlanificacionDTO;
import ar.edu.utn.frba.dds.scripts.dto.SolicitudPlanificacionDTO;import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;import java.util.List;

public class Client {
  private final String base_URL;
  private final HttpClient client;
  private final ObjectMapper objectMapper;

  public Client(HttpClient client, String URL) {
    this.base_URL = URL;
    this.client = client;
    this.objectMapper = new ObjectMapper();
  }

  public <T> T get(String path, Class<T> claseDeRespuesta)
      throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(base_URL + path))
        .GET().build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    validarRespuesta(response);
    return objectMapper.readValue(response.body(), claseDeRespuesta);
  }

  public <T> T post(String path, Object body, Class<T> claseDeRespuesta)
      throws IOException, InterruptedException {
    String json = objectMapper.writeValueAsString(body);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(base_URL + path))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    validarRespuesta(response);
    return objectMapper.readValue(response.body(), claseDeRespuesta);
  }

  public void put(String path, Object requestBody) throws IOException, InterruptedException {
    var json = objectMapper.writeValueAsString(requestBody);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(base_URL + path))
        .header("Content-Type", "application/json")
        .PUT(HttpRequest.BodyPublishers.ofString(json)).build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    validarRespuesta(response);
  }

  // ACÁ CHEQUEAMOS QUE LA RESPUESTA SEA LA ESPERADA
  private void validarRespuesta(HttpResponse<String> response) throws IOException {
    int status = response.statusCode();
    if(status >= 400) { // <-- codigo de client error responses
      throw new IOException("Error en la validacion de la respuesta del servicio de donaciones: "
      + status + response.body());
    }
  }

  public void notificarCambioEstado(int id, String nuevoEstado) {
    try {
      CambioEstadoDTO cambio = new CambioEstadoDTO();
      cambio.setNuevoEstado(nuevoEstado);
      put("/donaciones/" + id + "/estado", cambio);
    } catch(IOException | InterruptedException e) {
      System.err.println("Error al intentar notificar de cambio de Estado " +
          nuevoEstado + " a la donacion " + id + e.getMessage());
    }
  }

  public void notificarFallaDeEntrega(int idDonacion, String motivo) {
    try {
      CambioEstadoDTO cambio = new CambioEstadoDTO();
      cambio.setNuevoEstado(EstadoEntrega.FALLIDA.name());
      cambio.setMotivoFalla(motivo);
      put("/donaciones/" + idDonacion + "/estado", cambio);
    } catch(IOException | InterruptedException e) {
      System.err.println("Error al intentar notificar de entrega fallida a la donacion " +
          idDonacion + e.getMessage());
    }
  }

  public void solicitudPlanificacion(
      List<RequestPlanificacionDTO> donacionesAsignadas,
      List<Camion> camionesDisponibles
  ) {
    var tamanioLote = 100;
    var URLcallback = "/callback/planificaciones/";
    List<CamionDTO> camiones = camionesDisponibles.stream()
        .map(this::camionACamionDTO).toList();

    List<List<RequestPlanificacionDTO>> lotes = new ArrayList<>();
    for(int i=0; i<donacionesAsignadas.size(); i+=tamanioLote) {
      lotes.add(donacionesAsignadas.subList(i, Math.min(i + tamanioLote, donacionesAsignadas.size())));
    }
    for (List<RequestPlanificacionDTO> lote : lotes) {
      SolicitudPlanificacionDTO solicitud =
          new SolicitudPlanificacionDTO(lote, camiones, URLcallback);

      try {
        ConfirmationPlanificacionDTO confirmacion = post(
            URLcallback, solicitud, ConfirmationPlanificacionDTO.class);
        System.out.println("Lote enviado correctamente, id de ejecucion: " +
            confirmacion.getResponseId());
      } catch (IOException | InterruptedException e) {
        System.err.println("Error al solicitar planificacion para un lote de "
            + lote.size() + " donaciones: " + e.getMessage());
      }
    }
  }

  private CamionDTO camionACamionDTO(Camion camion) {
    CamionDTO c = new CamionDTO();
    c.setPatente(camion.getPatente());
    c.setAltura(camion.getAltura());
    c.setCapacidadCarga(camion.getCapacidadCarga());
    c.setCapacidadVolumen(camion.getCapacidadVolumen());
    return c;
  }

}
