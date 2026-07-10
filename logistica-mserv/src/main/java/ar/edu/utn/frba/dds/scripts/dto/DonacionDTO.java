package ar.edu.utn.frba.dds.scripts.dto;

import java.time.LocalDate;public class DonacionDTO {
  private String direccionEntidad;
  private int idEntidadAsignada;
  private LocalDate fechaVencimiento;

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

  public LocalDate getFechaVencimiento() {
    return fechaVencimiento;
  }

  public void setFechaVencimiento(LocalDate fechaVencimiento) {
    this.fechaVencimiento = fechaVencimiento;
  }
  
}
