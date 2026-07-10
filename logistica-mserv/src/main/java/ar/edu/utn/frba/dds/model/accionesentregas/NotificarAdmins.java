package ar.edu.utn.frba.dds.model.accionesentregas;

import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.EstadoEntrega;
import ar.edu.utn.frba.dds.server.Client;

import java.net.http.HttpClient;

public class NotificarAdmins implements  AccionesSobreEntregas{
  private final Client client;

  public NotificarAdmins() {
    this.client = new Client(HttpClient.newHttpClient());
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
