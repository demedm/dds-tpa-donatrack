package ar.edu.utn.frba.dds.dto;

import java.util.List;

public class PlanificacionManualRequest {
  private List<Long> camionesIds;

  public PlanificacionManualRequest() {}

  public List<Long> getCamionesIds() {
    return camionesIds;
  }

  public void setCamionesIds(List<Long> camionesIds) {
    this.camionesIds = camionesIds;
  }

}
