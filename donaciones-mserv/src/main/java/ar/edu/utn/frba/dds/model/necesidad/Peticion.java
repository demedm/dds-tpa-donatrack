package ar.edu.utn.frba.dds.model.necesidad;

import java.math.BigDecimal;
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

  @Column (precision = 10, scale = 3)
  private BigDecimal cantidadComprometida = BigDecimal.ZERO;

  @ManyToOne
  @JoinColumn(name = "necesidad_id")
  private Necesidad necesidad;

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

  public Long getId() {
    return id;
  }

  public BigDecimal getCantidadComprometida() {
    return cantidadComprometida;
  }

  public BigDecimal getCantidadRecibida() {
    return cantidadRecibida;
  }

  public BigDecimal getCantidadRequerida(){
    return cantidadRequerida;
  }

  public boolean estaCubierta(){
    return cantidadRecibida.compareTo(cantidadRequerida) >= 0;
  }

  // Para el matchmaking: lo recibido más lo que viene en camino todavía no alcanza
  public boolean faltaAsignar() {
    return cantidadRecibida.add(cantidadComprometida).compareTo(cantidadRequerida) < 0;
  }

  public String getSubclase(){
    return subclase;
  }

  // Se le asignó una donación que todavía no llegó
  public void comprometer(BigDecimal cantidad) {
    this.cantidadComprometida = this.cantidadComprometida.add(cantidad);
  }

  // La donación asignada no va a llegar (la entrega falló o volvió al depósito)
  public void liberar(BigDecimal cantidad) {
    this.cantidadComprometida = this.cantidadComprometida.subtract(cantidad).max(BigDecimal.ZERO);
  }

  // La donación asignada llegó: deja de estar en camino y pasa a recibida
  public void agregarCantidadRecibida(BigDecimal cantidad) {
    liberar(cantidad);
    this.cantidadRecibida = this.cantidadRecibida.add(cantidad);
  }

  public Necesidad getNecesidad() {
    return necesidad;
  }

  public void setNecesidad(Necesidad necesidad) {
    this.necesidad = necesidad;
  }
}