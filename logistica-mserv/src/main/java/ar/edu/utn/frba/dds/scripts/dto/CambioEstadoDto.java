package ar.edu.utn.frba.dds.scripts.dto;

public class CambioEstadoDto {
  private String nuevoEstado;
  private String motivoFalla;

  public CambioEstadoDto() {}

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
