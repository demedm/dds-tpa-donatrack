package ar.edu.utn.frba.dds.clients;

import ar.edu.utn.frba.dds.dto.DonacionDto;
import ar.edu.utn.frba.dds.dto.LoginRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class DonacionesHttpClient {
  private final java.net.http.HttpClient client;
  private final String baseUrl = "http://localhost:9001"; // El puerto de  donaciones-mserv
  private final ObjectMapper objectMapper;

  public DonacionesHttpClient() {
    this.client = HttpClient.newHttpClient();
    this.objectMapper = new ObjectMapper();
  }

  public HttpResponse<String> autenticarUsuario(String mail, String password) {
    LoginRequest request = new LoginRequest(mail, password);

    try {
      String body = objectMapper.writeValueAsString(request);

      HttpRequest httpRequest = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/auth/login"))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(body))
          .build();

      HttpResponse<String> response = client
          .send(httpRequest, HttpResponse.BodyHandlers.ofString());
      return response;
    } catch (Exception e) {
      System.err.println("Error al intentar enviar el request");
      return null;
    }
  }

  private List<DonacionDto> listarDonaciones(String path, String token) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + path))
          .timeout(Duration.ofSeconds(5))
          .header("Authorization", "Bearer " + token)
          .GET() //operacion
          .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() >= 400) {
        System.err.println("-> ¡ERROR! Donaciones respondió " + response.statusCode() + " en " + path);
        return List.of();
      }
      return objectMapper.readValue(response.body(), new TypeReference<List<DonacionDto>>() {});
    } catch (Exception e) {
      System.err.println("Error consultando Donaciones (" + path + "): " + e.getMessage());
      return List.of();
    }
  }
}
