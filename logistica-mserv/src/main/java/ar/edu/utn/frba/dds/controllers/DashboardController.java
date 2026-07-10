package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.repositories.CamionRepositorio;
import ar.edu.utn.frba.dds.scripts.dto.CamionDashboardDTO;
import io.javalin.http.Context;
import java.util.List;
import java.util.Map;

public class DashboardController {
  public void index(Context ctx) { }

  public void listarCamiones(Context ctx) {
    List<CamionDashboardDTO> camionesProcesados = CamionRepositorio.Instance.getFlota().stream()
        .map(camion -> {
          CamionDashboardDTO dto = new CamionDashboardDTO();
          dto.setPatente(camion.getPatente());
          dto.setEstado(camion.getEstado());

          if (camion.getRutaActual() != null) {
            dto.setPorcentajeAvance(camion.getRutaActual().calcularPorcentajeAvance());
          } else {
            dto.setPorcentajeAvance(0.0);
          }

          if (camion.getUbicacionActual() != null) {
            dto.setLatitud(camion.getUbicacionActual().getLatitud());
            dto.setLongitud(camion.getUbicacionActual().getLongitud());
            dto.setUltimaActualizacion(camion.getUbicacionActual().getTimestamp());
          }
          return dto;
        }).toList();

    ctx.render("listado.hbs", Map.of("camiones", camionesProcesados));
  }
}