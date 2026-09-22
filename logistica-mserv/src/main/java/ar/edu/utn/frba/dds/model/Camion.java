package ar.edu.utn.frba.dds.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class Camion {
  @Id
  @GeneratedValue
  private Long id;
  private String patente;
  private Double capacidadVolumen;
  private Double altura;
  private Double capacidadCarga;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado_camion")
  private EstadoCamion estado;

  public Camion() {}

  public EstadoCamion getEstado() {
    return estado;
  }

  public Camion(String patente, Double capacidadCarga, Double capacidadVolumen,
                Double altura) {
    this.altura = altura;
    this.capacidadCarga = capacidadCarga;
    this.capacidadVolumen = capacidadVolumen;
    this.estado = EstadoCamion.DISPONIBLE;
    this.patente = patente;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPatente() {
    return this.patente;
  }

  public Double getAltura() {
    return this.altura;
  }

  public Double getCapacidadVolumen() {
    return this.capacidadVolumen;
  }

  public Double getCapacidadCarga() {
    return this.capacidadCarga;
  }

  public void improvistoLogistico() {
    estado = EstadoCamion.EN_MANTENIMIENTO;
  }

  public void asignarRuta() {
    estado = EstadoCamion.RUTA_ASIGNADA;
  }

  public void iniciarRuta() {
    estado = EstadoCamion.REALIZANDO_ENTREGAS;
  }

  public void regresarDeposito() {
    estado = EstadoCamion.DISPONIBLE;
  }

}