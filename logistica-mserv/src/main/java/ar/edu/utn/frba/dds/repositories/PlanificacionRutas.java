package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.scripts.dto.RequestPlanificacionDTO;

import java.util.List;

public interface PlanificacionRutas {
  void solicitudPlanificacion(
      List<RequestPlanificacionDTO> donacionesAsignadas,
      List<Camion> camionesDisponibles
  );

}
