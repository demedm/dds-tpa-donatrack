package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.Entrega;

import java.util.List;

public interface PlanificacionRutas {
  void solicitudPlanificacion(
      List<Entrega> donacionesAsignadas,
      List<Camion> camionesDisponibles
  );

}
