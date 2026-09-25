package ar.edu.utn.frba.dds.model.accionesentregas;

import ar.edu.utn.frba.dds.main.ClientDonaciones;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.EstadoEntrega;
import java.net.http.HttpClient;

public class Notificar implements AccionesSobreEntregas {
  private final ClientDonaciones client;

  private static String urlDonaciones() {
    String url = System.getenv("DONACIONES_URL");
    return url != null ? url : "http://localhost:9001/";
  }

  public Notificar() {
    this.client = new ClientDonaciones(HttpClient.newHttpClient(), urlDonaciones());
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
        fallo.darMotivoFallo(), fallo.esReplanificable());
  }

}
