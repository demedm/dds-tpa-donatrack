package ar.edu.utn.frba.dds.scripts.dto.planificacion;

import ar.edu.utn.frba.dds.planificacion.ItemPlanificacion;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public class DonacionAPlanificarDto {
  private Long donacionId;
  private Long beneficiarioId;
  private String direccion;
  private LocalDate fechaVencimiento;
  private Double cantidad;
  private String unidadMedida;
  private String uso;

  public DonacionAPlanificarDto() {}

  public DonacionAPlanificarDto(@NotNull Long donacionId, @NotNull Long beneficiarioId,
                                String direccion, LocalDate fechaVencimiento, Double cantidad,
                                String unidadMedida, String uso) {
    this.donacionId = donacionId;
    this.beneficiarioId = beneficiarioId;
    this.direccion = direccion;
    this.fechaVencimiento = fechaVencimiento;
    this.cantidad = cantidad;
    this.unidadMedida = unidadMedida;
    this.uso = uso;
  }

  public static DonacionAPlanificarDto de(ItemPlanificacion i) {
    return new DonacionAPlanificarDto(
        i.getDonacionId(), i.getBeneficiarioId(), i.getDireccion(),
        i.getFechaVencimiento(), i.getCantidad(), i.getUnidadMedida(), i.getUso());
  }

  public Long getDonacionId() {
    return donacionId;
  }

  public void setDonacionId(Long donacionId) {
    this.donacionId = donacionId;
  }

  public Long getBeneficiarioId() {
    return beneficiarioId;
  }

  public void setBeneficiarioId(Long beneficiarioId) {
    this.beneficiarioId = beneficiarioId;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public LocalDate getFechaVencimiento() {
    return fechaVencimiento;
  }

  public void setFechaVencimiento(LocalDate fechaVencimiento) {
    this.fechaVencimiento = fechaVencimiento;
  }

  public Double getCantidad() {
    return cantidad;
  }

  public void setCantidad(Double cantidad) {
    this.cantidad = cantidad;
  }

  public String getUnidadMedida() {
    return unidadMedida;
  }

  public void setUnidadMedida(String unidadMedida) {
    this.unidadMedida = unidadMedida;
  }

  public String getUso() {
    return uso;
  }

  public void setUso(String uso) {
    this.uso = uso;
  }
}
