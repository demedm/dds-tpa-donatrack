
package planificador;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Planificador {

  public static void main(String[] args) {

    ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());

    HttpClient http = HttpClient.newHttpClient();

    Javalin app = Javalin.create();

    app.post("/planificaciones", ctx -> {

      JsonNode req;

      try {
        req = mapper.readTree(ctx.body());

        if (req == null
            || !req.hasNonNull("loteId")
            || !req.hasNonNull("callbackUrl")
            || !req.has("camiones")
            || !req.has("donaciones")) {

          ctx.status(400).result("Solicitud incompleta");
          return;
        }

        if (!req.get("camiones").isArray()
            || !req.get("donaciones").isArray()
            || req.get("camiones").isEmpty()
            || req.get("donaciones").isEmpty()) {

          ctx.status(400).result(
              "Se requieren camiones y donaciones"
          );
          return;
        }

      } catch (Exception e) {
        ctx.status(400).result("JSON inválido");
        return;
      }

      // Imprimir solicitud recibida
      try {
        System.out.println("\n========== NUEVA PLANIFICACIÓN ==========");
        System.out.println(
            mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(req)
        );
        System.out.println("==========================================\n");
      } catch (Exception e) {
        e.printStackTrace();
      }

      long loteId = req.get("loteId").asLong();

      // Respondemos inmediatamente al cliente
      ctx.status(202).result(
          "Lote " + loteId + " recibido correctamente"
      );

      // Simulamos el procesamiento del algoritmo
      new Thread(() -> {
        try {
          procesarPlanificacion(req, mapper, http);
        } catch (Exception e) {
          System.err.println(
              "Error procesando lote " + loteId
          );
          e.printStackTrace();
        }
      }, "planificador-lote-" + loteId).start();

    });

    app.start(9100);

    System.out.println(
        "Planificador simulado escuchando en puerto 9100"
    );
  }

  private static void procesarPlanificacion(
      JsonNode req,
      ObjectMapper mapper,
      HttpClient http
  ) throws Exception {

    Thread.sleep(2000);

    long loteId = req.get("loteId").asLong();
    String callback = req.get("callbackUrl").asText();

    JsonNode camiones = req.get("camiones");
    JsonNode donaciones = req.get("donaciones");

    List<Long> asignadas = new ArrayList<>();
    List<Long> sinAsignar = new ArrayList<>();

    List<Map<String, Object>> destinos = new ArrayList<>();

    // Simulamos que se asigna una donación de cada dos
    int i = 0;

    for (JsonNode d : donaciones) {

      long id = d.get("donacionId").asLong();

      if (i++ % 2 == 0) {

        destinos.add(Map.of(
            "direccion", d.get("direccion").asText(),
            "donacionIds", List.of(id)
        ));

        asignadas.add(id);

      } else {
        sinAsignar.add(id);
      }
    }

    List<Map<String, Object>> rutas = new ArrayList<>();

    if (!destinos.isEmpty()) {

      long camionId = camiones.get(0)
          .get("id").asLong();

      rutas.add(Map.of(
          "camionId", camionId,
          "destinos", destinos
      ));
    }

    // Fabricamos la respuesta del proveedor
    Map<String, Object> resp = Map.of(
        "loteId", loteId,
        "rutasPlanificadas", rutas,
        "donacionesNoPlanificadas", sinAsignar
    );

    String jsonRespuesta = mapper.writeValueAsString(resp);

    System.out.println("\n========== RESPUESTA FABRICADA ==========");
    System.out.println(
        mapper.writerWithDefaultPrettyPrinter()
            .writeValueAsString(resp)
    );
    System.out.println("=========================================\n");

    // Enviamos el resultado al callback de Logística
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(callback))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonRespuesta))
        .build();

    HttpResponse<String> response = http.send(
        request,
        HttpResponse.BodyHandlers.ofString()
    );

    System.out.println(
        "Respuesta del callback: HTTP " + response.statusCode()
    );

    System.out.println(
        "Contenido: " + response.body()
    );

    if (response.statusCode() < 200
        || response.statusCode() >= 300) {

      throw new RuntimeException(
          "Logística rechazó el callback: HTTP "
              + response.statusCode()
      );
    }

    System.out.println(
        "Lote " + loteId + " procesado correctamente"
    );
  }
}
