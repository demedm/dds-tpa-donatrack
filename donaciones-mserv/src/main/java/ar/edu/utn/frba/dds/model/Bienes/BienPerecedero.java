package ar.edu.utn.frba.dds.model.Bienes;

import java.util.Date;

public class BienPerecedero extends Bien {
  private Date fechaVencimiento;

  public BienPerecedero(Subcategoria subCategoria, String foto, String descripcion, Date fechaVencimiento) {
    super(subCategoria, foto, descripcion);
    this.fechaVencimiento = fechaVencimiento;
  }

  @Override
  protected Object getCriterioSegmentacion() {
    return this.fechaVencimiento;
  }
}