package ar.edu.utn.frba.dds.scripts.dto;

import java.time.LocalDate;

public class RequestPlanificacionDTO {
  private String id;
  private String direccion;
  private String idEntidad;
  private String idDonacion;
  private LocalDate fechaVencimiento;

  public RequestPlanificacionDTO() {}

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public String getEntidadId() {
    return idEntidad;
  }

  public void setIdEntidad(String idEntidad) {
    this.idEntidad = idEntidad;
  }

  public String getIdDonacion() {
    return idDonacion;
  }

  public void setIdDonacion(String idDonacion) {
    this.idDonacion = idDonacion;
  }

  public LocalDate getFechaVencimiento() {
    return fechaVencimiento;
  }

  public void setFechaVencimiento(LocalDate fechaVencimiento) {
    this.fechaVencimiento = fechaVencimiento;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
