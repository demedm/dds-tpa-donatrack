package ar.edu.utn.frba.dds.model.Bienes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name ="bienes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_bien")

public abstract class  Bien {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String descripcion;
  private String foto;

  @Embedded
  @JsonIgnore
  private Subcategoria subCategoria;

  protected Bien() {}

  public Bien(Subcategoria subCategoria, String foto, String descripcion) {
    this.subCategoria = subCategoria;
    this.foto = foto;
    this.descripcion = descripcion;
  }

  @JsonIgnore
  protected abstract Object getCriterioSegmentacion();

  @JsonIgnore
  public Criterio getCriterioDeAgrupacion() {
    return new Criterio(this.subCategoria, this.getCriterioSegmentacion());
  }

}



