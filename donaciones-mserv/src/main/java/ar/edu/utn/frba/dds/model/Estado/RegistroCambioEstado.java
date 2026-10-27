package ar.edu.utn.frba.dds.model.Estado;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class RegistroCambioEstado {

  @Column(name = "estado")
  private String estado;

  @Column(name = "fecha_hora")
  private LocalDateTime fechaHora;

  // JPA requiere un constructor vacío
  protected RegistroCambioEstado() {}

  public RegistroCambioEstado(String estado, LocalDateTime fechaHora) {
    this.estado = estado;
    this.fechaHora = fechaHora;
  }

  public String getEstado() { return estado; }
  public LocalDateTime getFechaHora() { return fechaHora; }
}