package ar.edu.utn.frba.dds.main.HttpClient;

import ar.edu.utn.frba.dds.exceptions.PlanificadorException;
import ar.edu.utn.frba.dds.grpc.RequestPlanificacionDto;
import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.scripts.dto.CamionDto;
import ar.edu.utn.frba.dds.scripts.dto.ConfirmationPlanificacionDto;
import ar.edu.utn.frba.dds.scripts.dto.planificacion.SolicitudPlanificacionDto;
import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientPlanificacion {
  private final Client client;

  public ClientPlanificacion(Client client) {
    this.client = client;
  }

  public void enviar(SolicitudPlanificacionDto solicitud) {
    try {
      client.post("/planificaciones", solicitud);
    } catch (IOException e) {
      throw new PlanificadorException("No se pudo entregar el lote "
          + solicitud.getLoteId() + e.getMessage());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new PlanificadorException("Envío interrumpido" + e.getMessage());
    }
  }
}
