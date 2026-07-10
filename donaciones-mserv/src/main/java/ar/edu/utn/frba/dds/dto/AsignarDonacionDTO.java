package ar.edu.utn.frba.dds.dto;

public class AsignarDonacionDTO {

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

  private String donacionSegmentadaId;
  private String entidadBeneficiariaId;

}