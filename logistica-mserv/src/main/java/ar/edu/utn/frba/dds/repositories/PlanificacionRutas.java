package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.scripts.dto.RequestPlanificacionDto;
import java.util.List;

public interface PlanificacionRutas {
  void solicitudPlanificacion(
      List<RequestPlanificacionDto> donacionesAsignadas,
      List<Camion> camionesDisponibles
  );

}
