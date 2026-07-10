package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.repositories.CamionRepositorio;
import ar.edu.utn.frba.dds.scripts.dto.TelemetriaDTO;
import io.javalin.http.Context;

public class MonitoreoController {

  public void recepcionarTelemetria(Context ctx) {
    String patente = ctx.pathParam("patente");
    TelemetriaDTO telemetria = ctx.bodyAsClass(TelemetriaDTO.class);

    if (!patente.equals(telemetria.getPatente())) {
      ctx.status(400).result("Inconsistencia en los datos de la patente.");
      return;
    }

    Camion camion = CamionRepositorio.Instance.findByPatente(patente);
    if (camion == null) {
      ctx.status(404).result("Camión no encontrado.");
      return;
    }

    camion.actualizarUbicacion(telemetria.getLatitud(), telemetria.getLongitud());
    ctx.status(200).result("Ubicación actualizada correctamente.");
  }
}