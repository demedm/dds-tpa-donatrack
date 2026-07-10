package ar.edu.utn.frba.dds.scripts.dto;

import ar.edu.utn.frba.dds.model.EstadoEntrega;

public class CambioEstadoDTO {
  private String nuevoEstado;
  private String motivoFalla;

  public CambioEstadoDTO() {}

  public void setNuevoEstado(String nuevoEstado) {
    this.nuevoEstado = nuevoEstado;
  }

  public String getNuevoEstado() {
    return this.nuevoEstado;
  }

  public String getMotivoFalla() {
    return motivoFalla;
  }

  public void setMotivoFalla(String motivoFalla) {
    this.motivoFalla = motivoFalla;
  }

}
