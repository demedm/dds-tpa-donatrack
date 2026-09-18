/*package ar.edu.utn.frba.dds.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MatchmakingJobRunner {

  private static final String URL = "http://localhost:9001/mathcmaking/procesar-pendietes";

  public static void main(String[] args) throws Exception {

    String token = System.getenv().getOrDefault("MATCHMAKING_JOB_TOKEN", "dev-secret-local");

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(URL))
        .header("X-Job-Token",token)
        .POST(HttpRequest.BodyPublishers.noBody())
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != 200){
      throw new RuntimeException("Fallo la corrida del matchmaking. HTTP "
          + response.statusCode() + ": " + response.body());
    }

    System.out.println("Matchmaking Ejecutando: " +response.body());
  }

}
 */
