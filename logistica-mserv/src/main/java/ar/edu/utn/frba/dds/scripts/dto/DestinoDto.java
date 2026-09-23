package ar.edu.utn.frba.dds.scripts.dto;

public class DestinoDto {
  private String direccion;
  private Long donacionId;

  public DestinoDto() {}

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public Long getDonacionId() {
    return donacionId;
  }

  public void setDonacionId(Long donacionId) {
    this.donacionId = donacionId;
  }

}
