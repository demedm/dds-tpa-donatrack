package ar.edu.utn.frba.dds.planificacion;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class ItemPlanificacion {
  @Id
  @GeneratedValue
  private Long id;

  @Column(unique = true)
  @NotNull
  private Long donacionId;

  private Long beneficiarioId;
  private String direccion;
  private LocalDate fechaVencimiento;
  private Double cantidad;
  private String unidadMedida;
  private String uso;

  @Enumerated(EnumType.STRING)
  private EstadoItem estado;

  private int intentosPlanif;

  @ManyToOne
  private LotePlanificacion lote;

  private LocalDateTime enviadoEn;

  public ItemPlanificacion() {
    this.estado = EstadoItem.PENDIENTE;
  }

  public ItemPlanificacion(@NotNull Long donacionId, @NotNull Long beneficiarioId,
                           @NotNull String direccion, LocalDate fechaVencimiento, Double cantidad,
                           String unidadMedida, String uso)
  {
    this.donacionId = donacionId;
    this.beneficiarioId = beneficiarioId;
    this.direccion = direccion;
    this.fechaVencimiento = fechaVencimiento;
    this.cantidad = cantidad;
    this.unidadMedida = unidadMedida;
    this.uso = uso;
    this.estado = EstadoItem.PENDIENTE;
  }

  public void enviarAPlanificar(LotePlanificacion lote) {
    this.lote = lote;
    this.estado = EstadoItem.EN_PLANIFICACION;
  }

  public void asignar() {
    this.estado = EstadoItem.ASIGNADO_RUTA;
  }

  public void devolverAPendiente() {
    this.estado = EstadoItem.PENDIENTE;
    this.intentosPlanif++;
  }

  public void marcarNoPlanificable() {
    this.estado = EstadoItem.NO_PLANIFICABLE;
  }

  public Long getId() {
    return id;
  }

  @NotNull
  public Long getDonacionId() {
    return donacionId;
  }

  public Long getBeneficiarioId() {
    return beneficiarioId;
  }

  public String getDireccion() {
    return direccion;
  }

  public LocalDate getFechaVencimiento() {
    return fechaVencimiento;
  }

  public Double getCantidad() {
    return cantidad;
  }

  public String getUnidadMedida() {
    return unidadMedida;
  }

  public String getUso() {
    return uso;
  }

  public int getIntentosPlanif() {
    return intentosPlanif;
  }

  public void setIntentosPlanif(int intentosPlanif) {
    this.intentosPlanif = intentosPlanif;
  }

  public LotePlanificacion getLote() {
    return lote;
  }

  public LocalDateTime getEnviadoEn() {
    return enviadoEn;
  }

  public void setEnviadoEn(LocalDateTime enviadoEn) {
    this.enviadoEn = enviadoEn;
  }

  public void setLote(LotePlanificacion lote) {
    this.lote = lote;
  }

  public void setEstado(EstadoItem estado) {
    this.estado = estado;
  }

  public EstadoItem getEstado() {
    return estado;
  }

  public void setDonacionId(@NotNull Long donacionId) {
    this.donacionId = donacionId;
  }

  public void setBeneficiarioId(Long beneficiarioId) {
    this.beneficiarioId = beneficiarioId;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public void setFechaVencimiento(LocalDate fechaVencimiento) {
    this.fechaVencimiento = fechaVencimiento;
  }

  public void setCantidad(Double cantidad) {
    this.cantidad = cantidad;
  }

  public void setUnidadMedida(String unidadMedida) {
    this.unidadMedida = unidadMedida;
  }

  public void setUso(String uso) {
    this.uso = uso;
  }
}
