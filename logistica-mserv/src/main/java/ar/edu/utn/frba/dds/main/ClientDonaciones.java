package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.model.EstadoEntrega;
import ar.edu.utn.frba.dds.scripts.dto.CambioEstadoDto;
import java.io.IOException;
import java.net.http.HttpClient;

public class ClientDonaciones extends Client {

  public ClientDonaciones(HttpClient client, String url) {
    super(client, url);
  }

  public void notificarCambioEstado(Long id, String nuevoEstado) {
    try {
      CambioEstadoDto cambio = new CambioEstadoDto();
      cambio.setNuevoEstado(nuevoEstado);
      put("/donaciones/" + id + "/estado", cambio);
    } catch (IOException | InterruptedException e) {
      System.err.println("Error al intentar notificar de cambio de Estado "
          + nuevoEstado + " a la donacion " + id + e.getMessage());
    }
  }

  public void notificarFallaDeEntrega(Long idDonacion, String motivo) {
    try {
      CambioEstadoDto cambio = new CambioEstadoDto();
      cambio.setNuevoEstado(EstadoEntrega.FALLIDA.name());
      cambio.setMotivoFalla(motivo);
      put("/donaciones/" + idDonacion + "/estado", cambio);
    } catch (IOException | InterruptedException e) {
      System.err.println("Error al intentar notificar de entrega fallida a la donacion "
          + idDonacion + e.getMessage());
    }
  }

}
