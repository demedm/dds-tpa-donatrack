package ar.edu.utn.frba.dds.Bienes;

import java.util.Date;

public class BienPerecedero extends ar.edu.utn.frba.dds.Bienes.Bien {
  private Date fechaVencimiento;

  public BienPerecedero(Subcategoria subCategoria, String foto, String descripcion, Date fechaVencimiento) {
    super(subCategoria, foto, descripcion);
    this.fechaVencimiento = fechaVencimiento;
  }

  @Override
  public Object getCriterioSegmentacion() {
    return this.fechaVencimiento;
  }
}