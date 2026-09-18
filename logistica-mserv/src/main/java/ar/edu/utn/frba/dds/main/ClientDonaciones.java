package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.model.EstadoEntrega;
import ar.edu.utn.frba.dds.scripts.dto.CambioEstadoDto;
import java.io.IOException;
import java.net.http.HttpClient;

public class ClientDonaciones extends Client {

  public ClientDonaciones(HttpClient client, String url) {
    super(client, url);
  }

  /*
    public void notificarInicioDeRuta(String donacionId, String rutaId, String urlMapa) {
      try {
        CambioEstadoDTO cambio = new CambioEstadoDTO();
        cambio.setNuevoEstado("EN_TRASLADO");
        cambio.setRutaId(rutaId);
        cambio.setUrlMapa(urlMapa);
        put("/donaciones/" + donacionId + "/estado", cambio);
      } catch (IOException | InterruptedException e) {
        System.err.println("Error al notificar inicio de ruta de la donacion "
            + donacionId + ": " + e.getMessage());
      }
    }
   */


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

  public void notificarFallaDeEntrega(Long idDonacion, String motivo, boolean replanificable) {
    try {
      CambioEstadoDto cambio = new CambioEstadoDto();
      cambio.setNuevoEstado(EstadoEntrega.FALLIDA.name());
      cambio.setMotivoFalla(motivo);
      cambio.setReplanificable(replanificable);
      put("/donaciones/" + idDonacion + "/estado", cambio);
    } catch (IOException | InterruptedException e) {
      System.err.println("Error al intentar notificar de entrega fallida a la donacion "
          + idDonacion + e.getMessage());
    }
  }

  /*
      public void notificarEntregaExitosa(String donacionId, String fechaHora, String patente) {
      try {
        CambioEstadoDTO cambio = new CambioEstadoDTO();
        cambio.setNuevoEstado("ENTREGADA");
        cambio.setFechaHora(fechaHora);
        cambio.setPatenteCamion(patente);
        put("/donaciones/" + donacionId + "/estado", cambio);
      } catch (IOException | InterruptedException e) {
        System.err.println("Error al notificar entrega exitosa de la donacion "
            + donacionId + ": " + e.getMessage());
      }
    }

   */


}
