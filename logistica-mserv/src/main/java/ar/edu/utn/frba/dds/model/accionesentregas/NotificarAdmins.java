package ar.edu.utn.frba.dds.model.accionesentregas;

import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.EstadoEntrega;
import ar.edu.utn.frba.dds.server.Client;

import java.net.http.HttpClient;

public class NotificarAdmins implements  AccionesSobreEntregas{
  private final Client client;

  private static String urlDonaciones() {
    String url = System.getenv("DONACIONES_URL");
    return url != null ? url : "http://localhost:9001/";
  }

  public NotificarAdmins() {
    this.client = new Client(HttpClient.newHttpClient(), urlDonaciones());
  }

  @Override
  public void notificarInicioRuta(Entrega entrega) {
    // No aplica
  }

  @Override
  public void notificarFalloEntrega(Entrega entrega) {
    // Todavia no tengo idea de como voy a hacer esto
  }
}
