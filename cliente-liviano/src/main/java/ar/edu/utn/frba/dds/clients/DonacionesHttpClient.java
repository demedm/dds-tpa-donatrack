package ar.edu.utn.frba.dds.clients;

import ar.edu.utn.frba.dds.dto.DonacionDto;
import ar.edu.utn.frba.dds.dto.LoginRequest;
import ar.edu.utn.frba.dds.dto.RegistroRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.UploadedFile;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

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

  private List<DonacionDto> listarDonaciones(String token) {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/donaciones"))
          .timeout(Duration.ofSeconds(5))
          .header("Authorization", "Bearer " + token)
          .GET() //operacion
          .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() >= 400) {
        System.err.println("[ERROR] Donaciones respondió " + response.statusCode());
        return List.of();
      }
      return objectMapper.readValue(response.body(), new TypeReference<List<DonacionDto>>() {});
    } catch (Exception e) {
      System.err.println("Error consultando Donaciones: " + e.getMessage());
      return List.of();
    }
  }

  public HttpResponse<String> importarDonantes(UploadedFile archivo) {
    try {
      byte[] contenido = archivo.content().readAllBytes();
      String nombreArchivo = archivo.filename();

      String boundary = "----DonaTrack" + UUID.randomUUID();

      ByteArrayOutputStream output = new ByteArrayOutputStream();

      output.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));

      output.write((
          "Content-Disposition: form-data; name=\"archivo\"; filename=\""
              + nombreArchivo + "\"\r\n"
      ).getBytes(StandardCharsets.UTF_8));

      output.write("Content-Type: text/csv\r\n\r\n"
          .getBytes(StandardCharsets.UTF_8));

      output.write(contenido);

      output.write(("\r\n--" + boundary + "--\r\n")
          .getBytes(StandardCharsets.UTF_8));

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/donantes/importar"))
          .header("Content-Type", "multipart/form-data; boundary=" + boundary)
          .timeout(Duration.ofSeconds(30))
          .POST(HttpRequest.BodyPublishers.ofByteArray(output.toByteArray()))
          .build();

      return client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (Exception e) {
      throw new RuntimeException("Error al enviar el CSV a Donaciones", e);
    }
  }
}
