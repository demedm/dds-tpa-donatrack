package ar.edu.utn.frba.dds.scripts.dto;

public class DonacionDTO {
  private String direccionEntidad;
  private int idEntidadAsignada;

  public DonacionDTO() {}

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
