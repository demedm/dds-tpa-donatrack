package ar.edu.utn.frba.dds.model.Bienes;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Objects;

@Entity
@Table(name ="subcategorias")

public class Subcategoria {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Categoria categoria;

  @Column(nullable = false)
  private String descripcion;

  @Enumerated(EnumType.STRING)
  private UnidadMedida unidad;

  protected Subcategoria() {}

  public Subcategoria(Categoria categoria, String descripcion, UnidadMedida unidad) {
    if (categoria == null || descripcion == null || unidad == null) {
      throw new IllegalArgumentException("Una subcategoría necesita categoría, descripción y unidad");
    }
    this.unidad = unidad;
    this.categoria = categoria;
    this.descripcion = descripcion;

  }

  public Long getId() {
    return id;
  }
  public Categoria getCategoria( ) {
    return categoria;
  }
  public String getDescripcion(){
    return descripcion;
  }

  public UnidadMedida getUnidad( ) {
    return unidad;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Subcategoria otra)) return false;
    return categoria == otra.categoria && descripcion.equals(otra.descripcion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(categoria, descripcion);
  }

}