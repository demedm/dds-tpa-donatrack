package ar.edu.utn.frba.dds.dto;

public class CambioEstadoDTO {
  private String nuevoEstado;
  private String motivoFallaEntrega;

  public CambioEstadoDTO() {}

  public void setNuevoEstado(String nuevoEstado) {
    this.nuevoEstado = nuevoEstado;
  }

  public void setMotivoFallaEntrega(String motivoFallaEntrega) {
    this.motivoFallaEntrega = motivoFallaEntrega;
  }

  public String getNuevoEstado() {
    return this.nuevoEstado;
  }

  public String getMotivoFallaEntrega() {
    return this.motivoFallaEntrega;
  }

}
