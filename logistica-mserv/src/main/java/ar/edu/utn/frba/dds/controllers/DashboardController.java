package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.EstadoRuta;
import ar.edu.utn.frba.dds.repositories.CamionRepositorio;
import ar.edu.utn.frba.dds.repositories.RutaRepositorio;
import ar.edu.utn.frba.dds.scripts.dto.CamionDashboardDto;
import io.javalin.http.Context;
import java.util.List;
import java.util.Map;

public class DashboardController {

  public void index(Context ctx) { }

  public void listarCamiones(Context ctx) {
    List<CamionDashboardDto> camionesProcesados = CamionRepositorio.Instance.mostrarTodos().stream()
        .map(camion -> {
          CamionDashboardDto dto = new CamionDashboardDto();
          dto.setPatente(camion.getPatente());
          dto.setEstado(camion.getEstado());

          var ruta = RutaRepositorio.Instance.buscarRutasDeCamionConEstado(
              camion.getId(), EstadoRuta.EN_CURSO);
          if (ruta != null) {
            dto.setPorcentajeAvance(ruta.calcularPorcentajeAvance());
          } else {
            dto.setPorcentajeAvance(0.0);
          }

          var ubicacion = CamionRepositorio.Instance.verUbicacionDeCamion(camion);
          if (ubicacion != null) {
            dto.setLatitud(ubicacion.getLatitud());
            dto.setLongitud(ubicacion.getLongitud());
            dto.setUltimaActualizacion(ubicacion.getTimestamp());
          }
          return dto;
        }).toList();

    ctx.render("listado.hbs", Map.of("camiones", camionesProcesados));
  }

}