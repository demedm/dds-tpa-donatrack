package ar.edu.utn.frba.dds.dto;

public class AsignarDonacionNecesidadDTO {
  private String donacionId;
  private Long necesidadId;

  public AsignarDonacionNecesidadDTO() {}

  public String getDonacionId() { 
    return donacionId; 
  }

  public void setDonacionId(String donacionId) {
    this.donacionId = donacionId;
  }

  public Long getNecesidadId() { 
    return necesidadId; 
  }

  public void setNecesidadId(Long necesidadId) {
    this.necesidadId = necesidadId;
  }
}