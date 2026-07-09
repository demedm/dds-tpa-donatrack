package ar.edu.utn.frba.dds.logistica.repositories;

import ar.edu.utn.frba.dds.logistica.modelo.Camion;
import ar.edu.utn.frba.dds.logistica.modelo.Entrega;

import java.util.List;

public interface PlanificacionRutas {
  void solicitudPlanificacion(
      List<Entrega> donacionesAsignadas,
      List<Camion> camionesDisponibles
  );

}
