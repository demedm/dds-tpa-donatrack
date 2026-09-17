package ar.edu.utn.frba.dds.model.accionesentregas;

import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.EstadoEntrega;
import ar.edu.utn.frba.dds.server.Client;

import java.net.http.HttpClient;

public class NotificarDonadorYDonante implements AccionesSobreEntregas{
  private final Client client;

  public NotificarDonadorYDonante() {
    this.client = new Client(HttpClient.newHttpClient(), "http://localhost:9001/");
  }

  @Override
  public void notificarInicioRuta(Entrega entrega) {
    client.notificarCambioEstado(entrega.getDonacionId(),
        EstadoEntrega.EN_TRASLADO.name());
  }

  @Override
  public void notificarFalloEntrega(Entrega entrega) {
    var fallo = entrega.getMotivoFallo();
    client.notificarFallaDeEntrega(entrega.getDonacionId(),
        fallo.darMotivoFallo());
    if(fallo.esReplanificable()) {
      // enviar a replanificar
    }
  }
}
