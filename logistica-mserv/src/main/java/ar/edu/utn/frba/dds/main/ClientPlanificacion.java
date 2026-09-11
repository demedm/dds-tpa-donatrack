package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.scripts.dto.CamionDto;
import ar.edu.utn.frba.dds.scripts.dto.ConfirmationPlanificacionDto;
import ar.edu.utn.frba.dds.scripts.dto.RequestPlanificacionDto;
import ar.edu.utn.frba.dds.scripts.dto.SolicitudPlanificacionDto;
import java.io.IOException;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

public class ClientPlanificacion extends Client {

  public ClientPlanificacion(HttpClient client, String url) {
    super(client, url);
  }

  public void solicitudPlanificacion(
      List<RequestPlanificacionDto> donacionesAsignadas,
      List<Camion> camionesDisponibles
  ) {
    var tamanioLote = 100;
    var urlCallback = "/callback/planificaciones/";
    List<CamionDto> camiones = camionesDisponibles.stream()
        .map(this::camionAdto).toList();

    List<List<RequestPlanificacionDto>> lotes = new ArrayList<>();
    for (int i = 0; i < donacionesAsignadas.size(); i += tamanioLote) {
      lotes.add(donacionesAsignadas.subList(i, Math.min(i + tamanioLote,
          donacionesAsignadas.size())));
    }
    for (List<RequestPlanificacionDto> lote : lotes) {
      SolicitudPlanificacionDto solicitud =
          new SolicitudPlanificacionDto(lote, camiones, urlCallback);
      try {
        ConfirmationPlanificacionDto confirmacion = post(
            urlCallback, solicitud, ConfirmationPlanificacionDto.class);
        System.out.println("Lote enviado correctamente, id de ejecucion: "
            + confirmacion.getResponseId());
      } catch (IOException | InterruptedException e) {
        System.err.println("Error al solicitar planificacion para un lote de "
            + lote.size() + " donaciones: " + e.getMessage());
      }
    }
  }

  private CamionDto camionAdto(Camion camion) {
    CamionDto c = new CamionDto();
    c.setPatente(camion.getPatente());
    c.setAltura(camion.getAltura());
    c.setCapacidadCarga(camion.getCapacidadCarga());
    c.setCapacidadVolumen(camion.getCapacidadVolumen());
    return c;
  }

}
