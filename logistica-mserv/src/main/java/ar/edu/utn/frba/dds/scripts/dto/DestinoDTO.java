package ar.edu.utn.frba.dds.scripts.dto;

import java.util.List;

public class DestinoDTO {
  private String id;
  private String direccion;
  private int donacionId;

  public DestinoDTO() {}

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public int getDonacionId() {
    return donacionId;
  }

  public void setDonacionId(int donacionId) {
    this.donacionId = donacionId;
  }

  public String getId() {
    return id;
  }
}
