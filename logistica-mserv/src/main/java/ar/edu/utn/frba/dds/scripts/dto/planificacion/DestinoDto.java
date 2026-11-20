package ar.edu.utn.frba.dds.scripts.dto.planificacion;

import java.util.List;

public class DestinoDto {
  private String direccion;
  private List<Long> donacionIds;

  public DestinoDto() {
    this.donacionIds = donacionIds == null ? List.of() : donacionIds;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public List<Long> getDonacionIds() {
    return donacionIds;
  }

  public void setDonacionIds(List<Long> donacionIds) {
    this.donacionIds = donacionIds;
  }
}
