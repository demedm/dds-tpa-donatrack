package ar.edu.utn.frba.dds.model;

import ar.edu.utn.frba.dds.model.fallaentrega.EntregaVencida;
import ar.edu.utn.frba.dds.model.fallaentrega.MotivoFallo;
import ar.edu.utn.frba.dds.model.fallaentrega.NoRecepcionada;
import ar.edu.utn.frba.dds.model.usuarios.EntidadBeneficiaria;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class Entrega {
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  private EntidadBeneficiaria entidadBeneficiaria;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado_entrega")
  private EstadoEntrega estado;

  private String direccion;
  private Long donacionId;
  private LocalDate fechaVencimiento;
  private boolean paradaVisitada = false;

  @Transient
  private MotivoFallo motivoFallo;

  private String foto;
  private LocalDateTime fechaHoraEntrega = null;
  private String entidadId;

  @ManyToOne
  private Camion camionQueEntrego = null;

  public Entrega(String direccion, Long idDonacion) {
    this.estado = EstadoEntrega.PENDIENTE;
    this.donacionId = idDonacion;
    this.direccion = direccion;
  }

  public Entrega() {}

  public String getEntidadId() {
    return entidadId;
  }

  public void setEntidadId(String entidadId) {
    this.entidadId = entidadId;
  }

  public LocalDate getFechaVencimiento() {
    return fechaVencimiento;
  }

  public void setFechaVencimiento(LocalDate fechaVencimiento) {
    this.fechaVencimiento = fechaVencimiento;
  }

  public Long getDonacionId() {
    return this.donacionId;
  }

  public void setParadaVisitada(boolean entregado) {
    this.paradaVisitada = entregado;
  }

  public String getDireccion() {
    return this.direccion;
  }

  public boolean getParadaVisitada() {
    return this.paradaVisitada;
  }

  public Long getId() {
    return this.id;
  }

  public void marcarComoIniciada() {
    estado = EstadoEntrega.EN_TRASLADO;
  }

  public void marcarComoEntregada(Camion camion, LocalDateTime fechaHoraEntrega) {
    paradaVisitada = true;
    this.camionQueEntrego = camion;
    this.fechaHoraEntrega = fechaHoraEntrega;
  }

  public void confirmarEntrega(String urlFoto) {
    estado = EstadoEntrega.ENTREGADA;
    setFoto(urlFoto);
  }

  public void marcarComoFallida(MotivoFallo motivo) {
    estado = EstadoEntrega.FALLIDA;
    setMotivoFallo(motivo);
  }

  public void marcarRegreso() {
    estado = EstadoEntrega.PENDIENTE;
  }

  public void marcarComoNoRecepcionada() {
    estado = EstadoEntrega.NO_RECIBIDA;
    marcarComoFallida(new NoRecepcionada());
  }

  public boolean estaVencida() {
    if (fechaVencimiento != null && fechaVencimiento.isBefore(LocalDate.now())) {
      marcarComoFallida(new EntregaVencida());
      return true;
    }
    return false;
  }

  public MotivoFallo getMotivoFallo() {
    return motivoFallo;
  }

  public void setMotivoFallo(MotivoFallo motivoFallo) {
    this.motivoFallo = motivoFallo;
  }

  public String getFoto() {
    return foto;
  }

  public void setFoto(String foto) {
    this.foto = foto;
  }

  public EstadoEntrega getEstado() {
    return estado;
  }

  public void setEstado(EstadoEntrega estado) {
    this.estado = estado;
  }

  public void setEntidadBeneficiaria(EntidadBeneficiaria entidadBeneficiaria) {
    this.entidadBeneficiaria = entidadBeneficiaria;
  }

}