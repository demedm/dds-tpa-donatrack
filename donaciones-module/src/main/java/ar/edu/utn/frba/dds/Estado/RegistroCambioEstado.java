package ar.edu.utn.frba.dds.Estado;

import java.time.LocalDateTime;

public class RegistroCambioEstado {
  private final String estado;
  private final LocalDateTime fechaHora;

  public RegistroCambioEstado(String estado, LocalDateTime fechaHora) {
    this.estado = estado;
    this.fechaHora = fechaHora;
  }

  public String getEstado() { return estado; }
  public LocalDateTime getFechaHora() { return fechaHora; }
}