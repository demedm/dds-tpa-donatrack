package ar.edu.utn.frba.dds.model.Bienes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "tipo"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = BienPerecedero.class, name = "perecedero"),
    @JsonSubTypes.Type(value = BienDuradero.class, name = "duradero")
})
public abstract class Bien {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String descripcion;
  private String foto;

  @Embedded
  private Subcategoria subCategoria;

  protected Bien() {}

  public Bien(Subcategoria subCategoria, String foto, String descripcion) {
    this.subCategoria = subCategoria;
    this.foto = foto;
    this.descripcion = descripcion;
  }

  public Long getId() {
    return id;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getFoto() {
    return foto;
  }

  public void setFoto(String foto) {
    this.foto = foto;
  }

  public Subcategoria getSubCategoria() {
    return subCategoria;
  }

  public void setSubCategoria(Subcategoria subCategoria) {
    this.subCategoria = subCategoria;
  }

  @JsonIgnore
  protected abstract Object getCriterioSegmentacion();

  @JsonIgnore
  public Criterio getCriterioDeAgrupacion() {
    return new Criterio(this.subCategoria, this.getCriterioSegmentacion());
  }
}


