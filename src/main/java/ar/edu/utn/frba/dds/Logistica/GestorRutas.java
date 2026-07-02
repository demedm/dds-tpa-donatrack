package ar.edu.utn.frba.dds.Logistica;

import ar.edu.utn.frba.dds.Bienes.DonacionSegmentada;

import java.util.ArrayList;
import java.util.List;

public class GestorRutas {
  private PlanificacionRutas planificadorRutas;
  private Flota flota;
  private List<Ruta> rutasPendientesAsignar = new ArrayList<>();
  private List<AccionesSobreRutas> accionesSobreRutas = new ArrayList<>();

  public void gestionarRutas(List<DonacionSegmentada> entregas) {
    this.planificadorRutas.solicitudPlanificacion(
        entregas, this.flota.getCamionesDisponibles());

  }

  public void recibirRespuesta(PlanificacionRutasResponse respuesta) {
    // !! REPLANIFICAR RUTAS NO ASIGNADAS (pendiente)
    respuesta.getRutas().stream()
        .map(RutaAdapter::rutaExternaToRuta)
        .forEach(ruta -> {
          boolean asignada = flota.asignarRutaACamion(ruta);
          accionesSobreRutas.forEach(accion ->
              accion.actualizarRuta(ruta, asignada));
        });
  }

}
