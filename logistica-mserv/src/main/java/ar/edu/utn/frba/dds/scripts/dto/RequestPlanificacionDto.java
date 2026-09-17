package ar.edu.utn.frba.dds.scripts.dto;

import java.time.LocalDate;

public class RequestPlanificacionDto {
  private String id;
  private String direccion;
  private Long idEntidad;
  private Long idDonacion;
  private LocalDate fechaVencimiento;

  public RequestPlanificacionDto() {}

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public Long getIdEntidad() {
    return idEntidad;
  }

  public void setIdEntidad(Long idEntidad) {
    this.idEntidad = idEntidad;
  }

  public Long getIdDonacion() {
    return idDonacion;
  }

  public void setIdDonacion(Long idDonacion) {
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
