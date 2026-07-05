package ar.edu.utn.frba.dds.logistica;

import ar.edu.utn.frba.dds.modelos.Camion;

import java.util.List;

public interface PlanificacionRutas {
  void solicitudPlanificacion(
      List<DonacionSegmentada>donacionesAsignadas,
      List<Camion>camionesDisponibles
  );

}
