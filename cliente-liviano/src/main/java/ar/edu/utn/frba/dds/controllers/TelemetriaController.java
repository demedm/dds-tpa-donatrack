package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.clients.GrpcClientImplementation.GrpcClientLogisticaImplementation;
import ar.edu.utn.frba.dds.repositorios.UbicacionesRepositorio;
import ar.edu.utn.frba.dds.scripts.dto.TelemetriaDto;
import io.grpc.Status;
import io.javalin.http.Context;

public class TelemetriaController {
  private static final GrpcClientLogisticaImplementation clientLogistica =
      new GrpcClientLogisticaImplementation();

  public void recepcionarTelemetria(Context ctx) {
    String patente = ctx.pathParam("patente");
    TelemetriaDto t = ctx.bodyAsClass(TelemetriaDto.class);

    if (!patente.equals(t.getPatente())) {
      ctx.status(400).result("Inconsistencia en los datos de la patente.");
      return;
    }
    if (t.getLatitud() < -90 || t.getLatitud() > 90
        || t.getLongitud() < -180 || t.getLongitud() > 180) {
      ctx.status(400).result("Coordenadas inválidas.");
      return;
    }

    String token = ctx.cookie("session");

    Status.Code resultado = clientLogistica.actualizarUbicacion( token, patente,
        t.getLatitud(), t.getLongitud());

    if (resultado == Status.Code.NOT_FOUND) {
      ctx.status(404).result("Camión no encontrado.");
      return;
    }
    if (resultado != Status.Code.OK) {
      ctx.status(502).result("No se pudo registrar la ubicación.");
      return;
    }

    // 2. Reflejar en el mapa
    UbicacionesRepositorio.Instance.guardar(patente, t.getLatitud(), t.getLongitud());
    ctx.status(200).result("Ubicación actualizada correctamente.");
  }
}
