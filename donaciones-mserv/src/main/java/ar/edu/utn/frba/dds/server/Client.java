package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.dto.DonacionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client {
  private final String base_URL = "http://localhost:9002/";
  private final HttpClient client;
  private final ObjectMapper objectMapper;

  public Client(HttpClient client) {
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

  public void enviarDonacionALogistica(int idDonacion, int idEntidadAsignada,
                                       String direccionEntidadAsignada) {
    try {
      DonacionDTO donacion = new DonacionDTO();
      donacion.setDireccionEntidad(direccionEntidadAsignada);
      donacion.setIdEntidadAsignada(idEntidadAsignada);
      post("/donaciones/" + idDonacion, donacion, DonacionDTO.class);
    } catch (InterruptedException | IOException | RuntimeException e) {
      System.err.println("Error al intentar enviar donacion " +
          idDonacion + " a logistica: " + e.getMessage());
    }

  }

}
