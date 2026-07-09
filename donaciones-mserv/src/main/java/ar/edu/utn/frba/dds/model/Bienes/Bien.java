package ar.edu.utn.frba.dds.model.Bienes;

public abstract class  Bien {
  private String Descripcion;
  private String Foto;
  private Subcategoria subCategoria;

  public Bien(Subcategoria subCategoria, String foto, String descripcion) {
    this.subCategoria = subCategoria;
    Foto = foto;
    Descripcion = descripcion;
  }

  public abstract Object getCriterioSegmentacion();

  public Criterio getCriterioDeAgrupacion() {
    return new Criterio(this.subCategoria, this.getCriterioSegmentacion());
  }
}



