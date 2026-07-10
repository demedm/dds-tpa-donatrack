package ar.edu.utn.frba.dds.scripts.dto;

import java.time.LocalDate;

public class RequestPlanificacionDTO {
  private String direccion;
  private int idEntidad;
  private int idDonacion;
  private LocalDate fechaVencimiento;

  public RequestPlanificacionDTO() {}

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

  public LocalDate getFechaVencimiento() {
    return fechaVencimiento;
  }

  public void setFechaVencimiento(LocalDate fechaVencimiento) {
    this.fechaVencimiento = fechaVencimiento;
  }

}
