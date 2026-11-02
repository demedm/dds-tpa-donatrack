package ar.edu.utn.frba.dds.dto;

import java.time.LocalDate;

public class DonacionSegmentadaDTO {
  private int idDonacion;
  private String direccionEntidad;
  private int idEntidadAsignada;
  private LocalDate fechaVencimiento;

  public DonacionSegmentadaDTO() {}

  // --- GETTERS Y SETTERS ---

  public int getIdDonacion() {
    return idDonacion;
  }

  public void setIdDonacion(int idDonacion) {
    this.idDonacion = idDonacion;
  }

  public String getDireccionEntidad() {
    return this.direccionEntidad;
  }

  public void setDireccionEntidad(String direccionEntidad) {
    this.direccionEntidad = direccionEntidad;
  }

  public int getIdEntidadAsignada() {
    return idEntidadAsignada;
  }

  public void setIdEntidadAsignada(int idEntidadAsignada) {
    this.idEntidadAsignada = idEntidadAsignada;
  }

  public LocalDate getFechaVencimiento() {
    return fechaVencimiento;
  }

  public void setFechaVencimiento(LocalDate fechaVencimiento) {
    this.fechaVencimiento = fechaVencimiento;
  }
}
