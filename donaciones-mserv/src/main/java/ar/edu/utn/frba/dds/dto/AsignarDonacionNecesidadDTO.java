package ar.edu.utn.frba.dds.dto;

public class AsignarDonacionNecesidadDTO {
  private Long donacionId;
  private Long necesidadId;

  public AsignarDonacionNecesidadDTO() {}

  public Long getDonacionId() {
    return donacionId; 
  }

  public void setDonacionId(Long donacionId) {
    this.donacionId = donacionId;
  }

  public Long getNecesidadId() { 
    return necesidadId; 
  }

  public void setNecesidadId(Long necesidadId) {
    this.necesidadId = necesidadId;
  }
}