package ar.edu.utn.frba.dds.clients;

import ar.edu.utn.frba.dds.dto.DonacionDto;
import ar.edu.utn.frba.dds.dto.DonanteDto;
import ar.edu.utn.frba.dds.dto.LoginRequest;
import ar.edu.utn.frba.dds.dto.RegistroRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

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

  public HttpResponse<String> registrarUsuario(RegistroRequest registro) {
    try {
      String body = objectMapper.writeValueAsString(registro);

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/auth/registro"))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(body))
          .build();

      return client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (Exception e) {
      throw new RuntimeException("Error al enviar el registro a Donaciones", e);
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

  // ---------- Pantalla del admin: ingresar donaciones ----------

  public List<DonacionDto> todasLasDonaciones(String token) {
    return listarDonaciones("/donaciones", token);
  }

  public List<DonanteDto> listarDonantes(String token) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/donantes"))
          .timeout(Duration.ofSeconds(5))
          .header("Authorization", "Bearer " + token)
          .GET()
          .build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() >= 400) {
        return List.of();
      }
      return objectMapper.readValue(response.body(), new TypeReference<List<DonanteDto>>() {});
    } catch (Exception e) {
      System.err.println("Error consultando los donantes: " + e.getMessage());
      return List.of();
    }
  }

  // Devuelve la donación creada, o null si donaciones la rechazó (el motivo queda en el log)
  public DonacionDto crearDonacion(Map<String, Object> donacion, String token) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/donaciones"))
          .timeout(Duration.ofSeconds(10))
          .header("Content-Type", "application/json")
          .header("Authorization", "Bearer " + token)
          .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(donacion)))
          .build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() >= 400) {
        System.err.println("-> Donaciones rechazó la donación: " + response.statusCode() + " " + response.body());
        return null;
      }
      return objectMapper.readValue(response.body(), DonacionDto.class);
    } catch (Exception e) {
      System.err.println("Error creando la donación: " + e.getMessage());
      return null;
    }
  }
}
