package ar.edu.utn.frba.dds.dto;

public class DonacionSegmentadaDTO {

  private String direccionEntidad;
  private int idEntidadAsignada;

  public DonacionSegmentadaDTO() {}



  public void setDireccionEntidad(String direccionEntidad) {
    this.direccionEntidad = direccionEntidad;
  }

  public void setIdEntidadAsignada(int idEntidadAsignada) {
    this.idEntidadAsignada = idEntidadAsignada;
  }

  public String getDireccionEntidad() {
    return this.direccionEntidad;
  }

  public int getIdEntidadAsignada() {
    return idEntidadAsignada;
  }

}
