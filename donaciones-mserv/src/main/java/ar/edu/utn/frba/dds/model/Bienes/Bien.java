package ar.edu.utn.frba.dds.model.Bienes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name ="bienes")

public class Bien {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String descripcion;
  private String foto;

  @ManyToOne
  @JoinColumn(name = "subcategoria_id")
  private Subcategoria subcategoria;

  @Column (precision = 10,scale = 3)
  private BigDecimal cantidad;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "criterio_id")
  private CriterioSegmentacion criterio;

  protected Bien() {}

  public Bien(Subcategoria subcategoria, String foto, String descripcion,
              BigDecimal cantidad, CriterioSegmentacion criterio) {
    if (subcategoria == null) {
      throw new IllegalArgumentException("Un bien necesita una subcategoría");
    }
    if (cantidad.compareTo(BigDecimal.ZERO) <= 0 ) {
      throw new IllegalArgumentException("La cantidad de un bien debe ser mayor a cero");
    }
    if (criterio == null) {
      throw new IllegalArgumentException("Todo bien necesita un criterio de segmentación");
    }
    this.subcategoria = subcategoria;
    this.foto = foto;
    this.descripcion = descripcion;
    this.cantidad = cantidad;
    this.criterio = criterio;
  }

  public Long getId() {
    return id;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public String getFoto() {
    return foto;
  }

  public Subcategoria getSubcategoria() {
    return subcategoria;
  }

  public BigDecimal getCantidad() {
    return cantidad;
  }

  public CriterioSegmentacion getCriterio() {
    return criterio;
  }

  public Criterio getCriterioDeAgrupacion() {
    return new Criterio(subcategoria, criterio.valor());
  }

}



