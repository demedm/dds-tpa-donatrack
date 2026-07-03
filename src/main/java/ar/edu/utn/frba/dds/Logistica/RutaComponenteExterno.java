package ar.edu.utn.frba.dds.Logistica;

import ar.edu.utn.frba.dds.Bienes.DonacionSegmentada;

import java.util.List;

public class RutaComponenteExterno {
  private List<String> direcciones;
  private List<DonacionSegmentada> entregas;
  private String patenteCamion;

  public List<String> getDirecciones() { return this.direcciones; }

  public List<DonacionSegmentada> getEntregas() { return this.entregas; }

  public String getPatenteCamion() { return this.patenteCamion; }

}
