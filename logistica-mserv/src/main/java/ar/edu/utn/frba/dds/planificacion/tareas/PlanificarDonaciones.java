package ar.edu.utn.frba.dds.planificacion.tareas;

import ar.edu.utn.frba.dds.exceptions.PlanificadorException;
import ar.edu.utn.frba.dds.main.Bootstrap;
import ar.edu.utn.frba.dds.main.HttpClient.Client;
import ar.edu.utn.frba.dds.main.HttpClient.ClientPlanificacion;
import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.EstadoCamion;
import ar.edu.utn.frba.dds.planificacion.EstadoItem;
import ar.edu.utn.frba.dds.planificacion.EstadoLote;
import ar.edu.utn.frba.dds.planificacion.ItemPlanificacion;
import ar.edu.utn.frba.dds.planificacion.LotePlanificacion;
import ar.edu.utn.frba.dds.repositories.planificacion.PlanificacionRepositorio;
import ar.edu.utn.frba.dds.scripts.dto.planificacion.CamionDisponibleDto;
import ar.edu.utn.frba.dds.scripts.dto.planificacion.DonacionAPlanificarDto;
import ar.edu.utn.frba.dds.scripts.dto.planificacion.SolicitudPlanificacionDto;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PlanificarDonaciones implements WithSimplePersistenceUnit {
  private static final int TAMANIO_LOTE = 100;
  private static final String CALLBACK_URL = System.getenv()
      .getOrDefault("CALLBACK_URL", "http://localhost:9002/callback/planificaciones");
  private static final ClientPlanificacion PLANIFICADOR = new ClientPlanificacion(
      new Client(
          HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build(),
          System.getenv().getOrDefault("PLANNER_URL", "http://localhost:9100")));

  public static void main(String[] args) {
    Bootstrap.init();
    ejecutar();
  }

  public static void ejecutar() {
    new PlanificarDonaciones().enviarLote();
  }

  void enviarLote() {
    // Fase 1: transacción corta que reserva todo y arma el request
    List<SolicitudPlanificacionDto> holder = new ArrayList<>();
    withTransaction(() -> holder.add(PlanificacionRepositorio.Instance
        .prepararSolicitud(TAMANIO_LOTE, CALLBACK_URL)));
    SolicitudPlanificacionDto solicitud = holder.get(0);
    if (solicitud == null) return;

    // Fase 2: HTTP fuera de la transacción
    try {
      PLANIFICADOR.enviar(solicitud);
    } catch (PlanificadorException e) {
      // Fase 3: compensación
      withTransaction(() -> PlanificacionRepositorio.Instance.revertirLote(solicitud.getLoteId()));
      throw e;
    }
  }

}
