package ar.edu.utn.frba.dds.logistica;

import java.util.List;

public class PlanificacionRutasResponse {
  private List<RutaComponenteExterno> rutas;
  private List<DonacionSegmentada> donacionesSinAsignar;
  private List<DonacionSegmentada> donacionesNoAsignadas;

  public List<RutaComponenteExterno> getRutas() {
    return this.rutas;
  }

  public List<DonacionSegmentada> getDonacionesSinAsignar() {
    return this.donacionesSinAsignar;
  }

  public List<DonacionSegmentada> getDonacionesNoAsignadas() {
    return this.donacionesNoAsignadas;
  }

}
