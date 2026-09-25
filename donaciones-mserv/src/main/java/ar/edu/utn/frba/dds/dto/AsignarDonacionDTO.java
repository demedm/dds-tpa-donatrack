package ar.edu.utn.frba.dds.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AsignarDonacionDTO {

  private Long donacionId;
  private Long necesidadId;

  @JsonIgnore
  private Long donacionSegmentadaId;
  @JsonIgnore
  private String entidadBeneficiariaId;


  public Long getDonacionSegmentadaId() {
    return donacionSegmentadaId;
  }

  public void setDonacionSegmentadaId(Long donacionSegmentadaId) {
    this.donacionSegmentadaId = donacionSegmentadaId;
  }

  public String getEntidadBeneficiariaId() {
    return entidadBeneficiariaId;
  }

  public void setEntidadBeneficiariaId(String entidadBeneficiariaId) {
    this.entidadBeneficiariaId = entidadBeneficiariaId;
  }

  @JsonIgnore
  public Long getDonacionId() { return donacionId; }
  @JsonIgnore
  public Long getNecesidadId() { return necesidadId; }

}