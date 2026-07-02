package ar.edu.utn.frba.dds.Logistica;

import ar.edu.utn.frba.dds.Bienes.DonacionSegmentada;

import java.util.List;

public class PlanificacionRutasResponse {
  private List<RutaComponenteExterno> rutas;
  private List<DonacionSegmentada> donacionesSinAsignar;

  public List<RutaComponenteExterno> getRutas() {
    return this.rutas;
  }

  public List<DonacionSegmentada> getDonacionesSinAsignar() {
    return this.donacionesSinAsignar;
  }

}
