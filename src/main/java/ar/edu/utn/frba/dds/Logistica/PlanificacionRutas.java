package ar.edu.utn.frba.dds.Logistica;

import ar.edu.utn.frba.dds.Bienes.DonacionSegmentada;

import java.util.List;

public interface PlanificacionRutas {
  void solicitudPlanificacion(
      List<DonacionSegmentada>donacionesAsignadas,
      List<Camion>camionesDisponibles
  );

}
