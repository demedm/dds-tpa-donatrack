package ar.edu.utn.frba.dds.model.Bienes;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Objects;

@Embeddable
public class Subcategoria {

  @Enumerated(EnumType.STRING)
  @Column(name = "categoria")
  private Categoria categoria;

  @Column(name ="subcategoria")
  private String descripcion;

  protected Subcategoria() {}

  public Subcategoria(Categoria categoria, String descripcion) {
    this.categoria = categoria;
    this.descripcion = descripcion;
  }

  public Categoria getCategoria() {
    return categoria;
  }

  public void setCategoria(Categoria categoria) {
    this.categoria = categoria;
  }

  public String getDescripcion() {
    return this.descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Subcategoria subCategoriaO)) return false;
    return Objects.equals(categoria, subCategoriaO.categoria) &&
        Objects.equals(descripcion, subCategoriaO.descripcion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(categoria, descripcion);
  }
}