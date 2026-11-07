package ar.edu.utn.frba.dds.clients;

import ar.edu.utn.frba.dds.dto.CamionDto;
import ar.edu.utn.frba.dds.dto.CamionRequest;
import ar.edu.utn.frba.dds.dto.DonacionDto;
import ar.edu.utn.frba.dds.dto.EntregaEnCursoDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class LogisticaHttpClient {
  private final java.net.http.HttpClient client;
  private final String baseUrl = "http://localhost:9002"; // El puerto de  donaciones-mserv
  private final ObjectMapper objectMapper;

  public LogisticaHttpClient() {
    this.client = HttpClient.newHttpClient();
    this.objectMapper = new ObjectMapper();
  }

  public List<CamionDto> listarCamiones(String token) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/camiones"))
          .timeout(Duration.ofSeconds(5))
          .header("Authorization", "Bearer " + token)
          .GET()
          .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() >= 400) {
        System.err.println("[ERROR] Logistica respondió " + response.statusCode());
        return List.of();
      }

      return objectMapper.readValue(response.body(), new TypeReference<List<CamionDto>>() {});
    } catch (Exception e) {
      System.err.println("Error consultando Logistica: " + e.getMessage());
      return List.of();
    }
  }

  public HttpResponse<String> registrarCamion(CamionRequest camion) {
    try {
      String body = objectMapper.writeValueAsString(camion);

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/camiones"))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(body))
          .build();

      return client.send(request, HttpResponse.BodyHandlers.ofString());

    } catch (Exception e) {
      throw new RuntimeException("Error al enviar el registro del camión a Logística", e);
    }
  }

  public List<EntregaEnCursoDto> obtenerEntregasEnCurso(Long idUsuario) {
    // 1. Pega directamente al microservicio de Logística (Puerto 9002)
    try {
      List<EntregaEnCursoDto> entregas = new ArrayList<>();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create("http://localhost:9002/api/usuarios/" + idUsuario + "/entregas"))
          .header("Accept", "application/json")
          .GET()
          .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        entregas = new ObjectMapper().readValue(response.body(), new TypeReference<List<EntregaEnCursoDto>>() {});
      }
    } catch (Exception e) {
      System.err.println("Error al consultar Logística MS: " + e.getMessage());
    }
    return null;
  }

}
