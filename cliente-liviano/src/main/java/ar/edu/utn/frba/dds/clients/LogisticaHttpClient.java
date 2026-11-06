package ar.edu.utn.frba.dds.clients;

import java.net.http.HttpClient;

public class LogisticaHttpClient {
  private final java.net.http.HttpClient client;
  private final String baseUrl = "http://localhost:9002"; // El puerto de  logistica-mserv

  public LogisticaHttpClient() {
    this.client = HttpClient.newHttpClient();
  }
}
