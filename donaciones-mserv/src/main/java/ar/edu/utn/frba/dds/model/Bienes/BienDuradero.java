package ar.edu.utn.frba.dds.model.Bienes;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@DiscriminatorValue("DURADERO")

public class BienDuradero extends Bien {

  @Enumerated(EnumType.STRING)
  @Column(name = "estado_uso")
  private EstadoUso estado;

  public BienDuradero(Subcategoria subCategoria, String foto, String descripcion, EstadoUso estado) {
    super(subCategoria, foto, descripcion);
    this.estado = estado;
  }

  @Override
  protected Object getCriterioSegmentacion() {
    return this.estado;
  }

}
