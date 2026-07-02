package ar.edu.utn.frba.dds.Bienes;

public abstract class  Bien {
  private String Descripcion;
  private String Foto;
  private Subcategoria subCategoria;
  private Integer cantidad;

  public Bien(Subcategoria subCategoria, Integer cantidad, String foto, String descripcion) {
    this.subCategoria = subCategoria;
    this.cantidad = cantidad;
    Foto = foto;
    Descripcion = descripcion;
  }

  public abstract Object getCriterioSegmentacion();

  public Criterio getClaveAgrupacion() {
    return new Criterio(this.subCategoria, this.getCriterioSegmentacion());
  }
}



