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
  private long id;

  private String Descripcion;
  private String Foto;

  @Embedded
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



