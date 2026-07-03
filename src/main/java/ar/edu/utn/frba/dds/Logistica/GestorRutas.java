package ar.edu.utn.frba.dds.Logistica;

import ar.edu.utn.frba.dds.Bienes.DonacionSegmentada;

import java.util.ArrayList;
import java.util.List;

public class GestorRutas {
  private PlanificacionRutas planificadorRutas;
  private Flota flota;
  private List<Ruta> rutasPendientesAsignar = new ArrayList<>();
  private List<AccionesSobreRutas> accionesSobreRutas = new ArrayList<>();

  private List<DonacionSegmentada> donacionesSinAsignar = new ArrayList<>();

  public void gestionarRutas(List<DonacionSegmentada> entregas) {
    int tamanioLote = 100;

    for (int i = 0; i < entregas.size(); i += tamanioLote) {
      List<DonacionSegmentada> lote = entregas.subList(i, Math.min(i + tamanioLote, entregas.size()));

      this.planificadorRutas.solicitudPlanificacion(
          lote, this.flota.getCamionesDisponibles()
      );
    }
  }

  public void recibirRespuesta(PlanificacionRutasResponse respuesta) {

    if (respuesta.getDonacionesSinAsignar() != null) {
      this.donacionesSinAsignar.addAll(respuesta.getDonacionesSinAsignar());

    }

    respuesta.getRutas().stream()
        .map(RutaAdapter::rutaExternaToRuta)
        .forEach(ruta -> {
          boolean asignada = flota.asignarRutaACamion(ruta);
          accionesSobreRutas.forEach(accion ->
              accion.actualizarRuta(ruta, asignada));
        });
  }

  public List<DonacionSegmentada> getDonacionesSinAsignar() {
    return donacionesSinAsignar;
  }
}