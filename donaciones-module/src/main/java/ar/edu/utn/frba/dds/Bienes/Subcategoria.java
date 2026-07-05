package ar.edu.utn.frba.dds.Bienes;

import java.util.Objects;

public class Subcategoria {
  private Categoria categoria;
  private String descripcion;

  public Subcategoria(Categoria categoria, String descripcion) {
    this.categoria = categoria;
    this.descripcion = descripcion;
  }

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

  public String getDescripcion() {
    return this.descripcion;
  }
}