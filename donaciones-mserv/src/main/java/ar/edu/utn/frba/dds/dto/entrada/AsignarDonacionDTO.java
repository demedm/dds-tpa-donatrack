package ar.edu.utn.frba.dds.dto.entrada;

public class AsignarDonacionDTO {

  private Long donacionId;
  private Long necesidadId;

  private Long donacionSegmentadaId;
  private Long entidadBeneficiariaId;


  public Long getDonacionSegmentadaId() {
    return donacionSegmentadaId;
  }

  public void setDonacionSegmentadaId(Long donacionSegmentadaId) {
    this.donacionSegmentadaId = donacionSegmentadaId;
  }

  public Long getEntidadBeneficiariaId() {
    return entidadBeneficiariaId;
  }

  public void setEntidadBeneficiariaId(Long entidadBeneficiariaId) {
    this.entidadBeneficiariaId = entidadBeneficiariaId;
  }


  public Long getDonacionId() { return donacionId; }

  public Long getNecesidadId() { return necesidadId; }

}