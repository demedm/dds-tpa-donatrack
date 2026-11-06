package ar.edu.utn.frba.dds.model.necesidad;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;


@Entity
@Table(name = "peticiones")

public class Peticion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String subclase;

  @Column (precision = 10, scale = 3)
  private BigDecimal cantidadRequerida;

  @Column (precision = 10, scale = 3)
  private BigDecimal cantidadRecibida = BigDecimal.ZERO;

  @ManyToOne
  @JoinColumn(name = "necesidad_id")
  private Necesidad necesidad;

  @ElementCollection
  @CollectionTable(
      name = "peticion_donaciones",
      joinColumns = @JoinColumn(name = "peticion_id")
  )
  @Column(name = "donacion_id")
  private List<String> donacionesAsignados = new ArrayList<>();

  public Peticion(){};

  public Peticion(String subclase, BigDecimal cantidadRequerida) {
    this.cantidadRequerida=cantidadRequerida;
    this.subclase=subclase;
  }

  public void setSubclase(String subclase) {
    this.subclase = subclase;
  }

  public void setCantidadRequerida(BigDecimal cantidadRequerida) {
    this.cantidadRequerida = cantidadRequerida;
  }

  public void setCantidadRecibida(BigDecimal cantidadRecibida) {
    this.cantidadRecibida = cantidadRecibida;
  }

  public void setDonacionesAsignados(List<String> donacionesAsignados) {
    this.donacionesAsignados.addAll(donacionesAsignados);
  }

  public BigDecimal getCantidadRecibida() {
    return cantidadRecibida;
  }

  public List<String> getDonacionesAsignados() {
    return donacionesAsignados;
  }

  public List<String> getDonacionesAsignadas() {
    return this.donacionesAsignados;
  }

  public BigDecimal getCantidadRequerida(){
    return cantidadRequerida;
  }

  public boolean estaCubierta(){
    return cantidadRecibida.compareTo(cantidadRequerida) >= 0;
  }

  public String getSubclase(){
    return subclase;
  }

  public void agregarCantidadRecibida (BigDecimal cantidad, String idDonacion){
    this.cantidadRecibida = this.cantidadRecibida.add(cantidad);
    this.donacionesAsignados.add(idDonacion);
  }

  public Necesidad getNecesidad() {
    return necesidad;
  }

  public void setNecesidad(Necesidad necesidad) {
    this.necesidad = necesidad;
  }
}