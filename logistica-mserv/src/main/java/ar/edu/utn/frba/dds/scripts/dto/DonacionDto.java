package ar.edu.utn.frba.dds.scripts.dto;

import java.time.LocalDate;

public class DonacionDto {
  private String direccionEntidad;
  private Long idEntidadAsignada;
  private LocalDate fechaVencimiento;

  public DonacionDto() {}

  public void setDireccionEntidad(String direccionEntidad) {
    this.direccionEntidad = direccionEntidad;
  }

  public void setIdEntidadAsignada(Long idEntidadAsignada) {
    this.idEntidadAsignada = idEntidadAsignada;
  }

  public String getDireccionEntidad() {
    return this.direccionEntidad;
  }

  public Long getIdEntidadAsignada() {
    return idEntidadAsignada;
  }

  public LocalDate getFechaVencimiento() {
    return fechaVencimiento;
  }

  public void setFechaVencimiento(LocalDate fechaVencimiento) {
    this.fechaVencimiento = fechaVencimiento;
  }
  
}
