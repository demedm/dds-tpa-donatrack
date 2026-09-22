package ar.edu.utn.frba.dds.scripts.dto;

public class DestinoDto {
  private String id;
  private String direccion;
  private String donacionId;

  public DestinoDto() {}

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public String getDonacionId() {
    return donacionId;
  }

  public void setDonacionId(String donacionId) {
    this.donacionId = donacionId;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}
