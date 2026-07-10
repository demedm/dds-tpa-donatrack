package ar.edu.utn.frba.dds.scripts.dto;

import ar.edu.utn.frba.dds.model.EstadoEntrega;

public class CambioEstadoDTO {
  private String nuevoEstado;

  public CambioEstadoDTO() {}

  public void setNuevoEstado(String nuevoEstado) {
    this.nuevoEstado = nuevoEstado;
  }

  public String getNuevoEstado() {
    return this.nuevoEstado;
  }

}
