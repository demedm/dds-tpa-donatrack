package ar.edu.utn.frba.dds.model.Bienes;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("ESTADO")

public class PorEstado extends CriterioSegmentacion{

  @Enumerated(EnumType.STRING)
  @Column(name= "estado_uso")
  private EstadoUso estado;

  protected PorEstado(){}

  public PorEstado(EstadoUso estado) {
    if (estado == null) {
      throw new IllegalArgumentException("Un bien duradero necesita estado de uso");
    }
    this.estado = estado;
  }

  public EstadoUso getEstado() {
    return estado;
  }

  @Override
  public Object valor() { return  estado; }


}
