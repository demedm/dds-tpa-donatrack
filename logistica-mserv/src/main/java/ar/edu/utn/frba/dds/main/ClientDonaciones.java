package ar.edu.utn.frba.dds.main;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClientDonaciones {

  private final HttpClient client;
  private final String baseUrl = "http://localhost:9001"; // El puerto de donaciones-mserv

  public ClientDonaciones() {
    this.client = HttpClient.newHttpClient();
  }
  public boolean notificarCambioEstado(Long idDonacion, String nuevoEstado) {
    try {
      String jsonPayload = "{\"nuevoEstado\":\"" + nuevoEstado + "\"}";

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/donaciones/" + idDonacion + "/estado"))
          .header("Content-Type", "application/json")
          .PUT(HttpRequest.BodyPublishers.ofString(jsonPayload))
          .build();

      HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

      if (response.statusCode() >= 400) {
        System.err.println("-> ¡ERROR! Donaciones rechazó el aviso. Status: " + response.statusCode());
        return false;
      } else {
        System.out.println("-> Aviso a Donaciones: Donacion " + idDonacion + " actualizada a " + nuevoEstado);
        return true;
      }
    } catch (Exception e) {
      System.err.println("Error avisando a Donaciones estado " + nuevoEstado + ": " + e.getMessage());
      return false;
    }
  }

  public boolean notificarFallaDeEntrega(Long idDonacion, String motivo, boolean replanificable) {
    try {
      String jsonPayload = String.format(
          "{\"nuevoEstado\":\"FALLIDA\", \"motivoFalla\":\"%s\", \"replanificable\":%b}",
          motivo, replanificable
      );

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/donaciones/" + idDonacion + "/estado"))
          .header("Content-Type", "application/json")
          .PUT(HttpRequest.BodyPublishers.ofString(jsonPayload))
          .build();

      HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());

      if (response.statusCode() >= 400) {
        System.err.println("-> ¡ERROR! Donaciones rechazó el aviso de falla. Status: " + response.statusCode());
        return false;
      } else {
        System.out.println("-> Aviso a Donaciones: Donacion " + idDonacion + " FALLIDA (" + motivo + ")");
        return true;
      }
    } catch (Exception e) {
      System.err.println("Error avisando a Donaciones falla: " + e.getMessage());
      return false;
    }
  }
}