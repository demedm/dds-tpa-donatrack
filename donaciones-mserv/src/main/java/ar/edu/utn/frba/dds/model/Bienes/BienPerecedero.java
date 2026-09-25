package ar.edu.utn.frba.dds.model.Bienes;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@DiscriminatorValue("PERECEDERO")

public class BienPerecedero extends Bien {

  @Temporal(TemporalType.DATE)
  @Column(name = "fecha_vencimiento")
  private Date fechaVencimiento;

  protected BienPerecedero() {}

  public BienPerecedero(Subcategoria subCategoria, String foto, String descripcion, Date fechaVencimiento) {
    super(subCategoria, foto, descripcion);
    this.fechaVencimiento = fechaVencimiento;
  }

  @Override
  protected Object getCriterioSegmentacion() {
    return this.fechaVencimiento;
  }
}