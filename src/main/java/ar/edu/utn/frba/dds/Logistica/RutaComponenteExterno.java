package ar.edu.utn.frba.dds.Logistica;

import ar.edu.utn.frba.dds.Bienes.DonacionSegmentada;

import java.util.List;

public class RutaComponenteExterno {
  private List<String> direcciones;
  private List<DonacionSegmentada> entregas;

  public void setDirecciones(List<String> direcciones) {
    this.direcciones = direcciones;
  }

  public List<String> getDirecciones() { return direcciones; }

  public void setEntregas(List<DonacionSegmentada> entregas) {
    this.entregas = entregas;
  }

  public List<DonacionSegmentada> getEntregas() { return entregas; }

}
