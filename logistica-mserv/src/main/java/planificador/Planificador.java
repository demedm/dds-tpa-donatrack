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
    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    HttpClient http = HttpClient.newHttpClient();

    Javalin.create().post("/planificaciones", ctx -> {
      JsonNode req = mapper.readTree(ctx.body());
      ctx.status(202);                       // responde ya, procesa después

      new Thread(() -> {
        try {
          Thread.sleep(2000);                // simula el cómputo
          long loteId = req.get("loteId").asLong();
          String callback = req.get("callbackUrl").asText();
          JsonNode camiones = req.get("camiones");
          JsonNode donaciones = req.get("donaciones");

          // reparto trivial: una ruta con el primer camión; la mitad de las donaciones va "sin asignar"
          List<Long> asignadas = new ArrayList<>(), sinAsignar = new ArrayList<>();
          List<Map<String, Object>> destinos = new ArrayList<>();
          int i = 0;
          for (JsonNode d : donaciones) {
            long id = d.get("donacionId").asLong();
            if (i++ % 2 == 0) {
              destinos.add(Map.of("direccion", d.get("direccion").asText(), "donacionIds", List.of(id)));
              asignadas.add(id);
            } else {
              sinAsignar.add(id);
            }
          }

          Map<String, Object> resp = Map.of(
              "loteId", loteId,
              "rutasPlanificadas", destinos.isEmpty() ? List.of()
                  : List.of(Map.of("camionId", camiones.get(0).get("id").asLong(), "destinos", destinos)),
              "donacionesNoPlanificadas", sinAsignar);

          http.send(HttpRequest.newBuilder(URI.create(callback))
                  .header("Content-Type", "application/json")
                  .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(resp)))
                  .build(),
              HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      ).start();
    }
    ).start(9100);
  }
}
