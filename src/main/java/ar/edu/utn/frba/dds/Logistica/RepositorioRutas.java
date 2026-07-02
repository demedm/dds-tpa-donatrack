package ar.edu.utn.frba.dds.Logistica;

import ar.edu.utn.frba.dds.Bienes.DonacionSegmentada;

import java.util.ArrayList;
import java.util.List;

public class RepositorioRutas {
  private PlanificacionRutas planificadorRutas;
  private Flota flota;
  private List<Ruta> rutasPendientesAsignar = new ArrayList<>();

  public void gestionarRutas(List<DonacionSegmentada> entregas) {
    this.planificadorRutas.solicitudPlanificacion(
        entregas, this.flota.getCamionesDisponibles());

  }

  public void recibirRespuesta(PlanificacionRutasResponse respuesta) {
    // asignarRutas devuelve las que no pudieron asignarse
    List<Ruta> rutasNoAsignadas = this.flota.asignarRutasACamiones(respuesta.getRutas());
    this.rutasPendientesAsignar.addAll(rutasNoAsignadas); // !! REPLANIFICAR RUTAS NO ASIGNADAS (pendiente)

  }

}
