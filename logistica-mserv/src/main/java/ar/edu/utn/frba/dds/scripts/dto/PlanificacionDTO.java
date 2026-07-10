package ar.edu.utn.frba.dds.scripts.dto;

public class PlanificacionDTO {
  private String direccion;
  private int idEntidad;
  private int idDonacion;

  public PlanificacionDTO() {}

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public int getIdEntidad() {
    return idEntidad;
  }

  public void setIdEntidad(int idEntidad) {
    this.idEntidad = idEntidad;
  }

  public int getIdDonacion() {
    return idDonacion;
  }

  public void setIdDonacion(int idDonacion) {
    this.idDonacion = idDonacion;
  }

}
