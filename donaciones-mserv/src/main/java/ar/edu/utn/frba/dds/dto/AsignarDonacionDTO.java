package ar.edu.utn.frba.dds.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AsignarDonacionDTO {
  private String donacionId;
  private Long necesidadId;

  public String getDonacionSegmentadaId() {
    return donacionSegmentadaId;
  }

  public void setDonacionSegmentadaId(String donacionSegmentadaId) {
    this.donacionSegmentadaId = donacionSegmentadaId;
  }

  public String getEntidadBeneficiariaId() {
    return entidadBeneficiariaId;
  }

  public void setEntidadBeneficiariaId(String entidadBeneficiariaId) {
    this.entidadBeneficiariaId = entidadBeneficiariaId;
  }
  @JsonIgnore
  private String donacionSegmentadaId;
  @JsonIgnore
  private String entidadBeneficiariaId;
  @JsonIgnore
  public String getDonacionId() { return donacionId; }
  @JsonIgnore
  public Long getNecesidadId() { return necesidadId; }

}