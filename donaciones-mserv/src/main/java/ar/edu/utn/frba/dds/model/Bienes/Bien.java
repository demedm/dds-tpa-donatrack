package ar.edu.utn.frba.dds.model.Bienes;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class  Bien {
  private String Descripcion;
  private String Foto;
  @JsonIgnore
  private Subcategoria subCategoria;

  public Bien(Subcategoria subCategoria, String foto, String descripcion) {
    this.subCategoria = subCategoria;
    Foto = foto;
    Descripcion = descripcion;
  }
  @JsonIgnore
  protected abstract Object getCriterioSegmentacion();
  @JsonIgnore
  public Criterio getCriterioDeAgrupacion() {
    return new Criterio(this.subCategoria, this.getCriterioSegmentacion());
  }

}



