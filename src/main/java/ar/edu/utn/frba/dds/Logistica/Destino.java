package ar.edu.utn.frba.dds.Logistica;

import ar.edu.utn.frba.dds.Bienes.DonacionSegmentada;

public class Destino {
  private String direccion;
  private DonacionSegmentada donacion;
  private boolean visitado = false;

  public Destino(String direccion, DonacionSegmentada donacion) {
    this.donacion = donacion;
    this.direccion = direccion;
  }

  public DonacionSegmentada getDonacion() {
    return donacion;
  }

  public void setVisitado(boolean visitado) {
    this.visitado = visitado;
  }

  public String getDireccion() { return this.direccion; }

  public boolean getVisitado() { return this.visitado; }

}
