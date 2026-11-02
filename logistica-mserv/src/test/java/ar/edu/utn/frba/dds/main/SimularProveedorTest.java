package ar.edu.utn.frba.dds.main;

import org.junit.jupiter.api.Test;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SimularProveedorTest {

  @Test
  public void testSimularRespuestaDelProveedor() {
    try {
      // 1. Armamos un JSON idéntico a lo que devolvería el proveedor externo
      // Contiene 1 ruta exitosa (con la donación 100) y 1 donación rebotada (la 101)
      String jsonRespuestaProveedor = """
{
  "rutasPlanificadas": [
    {
      "camion": { "patente": "aaaaAAAA" },
      "destinos": [
        { "direccion": "Medrano 951, CABA", "donacionId": 100 }
      ]
    }
  ],
  "donacionesNoPlanificadas": [
    { "idDonacion": 101, "direccion": "Av. Rivadavia 4500, CABA" }
  ]
}
""";

      // 2. Disparamos la petición hacia nuestro propio microservicio de Logística
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create("http://localhost:9002/callback/planificaciones/"))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(jsonRespuestaProveedor))
          .build();

      System.out.println("Enviando planificación del proveedor a Logística...");
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      System.out.println("Respuesta de Logística: " + response.statusCode());

    } catch (Exception e) {
      System.err.println("Error en el test: " + e.getMessage());
    }
  }
}