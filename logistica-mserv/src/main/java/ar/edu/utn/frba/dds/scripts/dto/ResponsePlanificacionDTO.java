package ar.edu.utn.frba.dds.scripts.dto;

import java.util.List;

public class ResponsePlanificacionDTO {
  private List<RutaPlanificadaDTO> rutasPlanificadas;
  private List<RequestPlanificacionDTO> donacionesNoPlanificadas;

  public ResponsePlanificacionDTO() {}

  public List<RutaPlanificadaDTO> getRutasPlanificadas() {
    return rutasPlanificadas;
  }

  public void setRutasPlanificadas(List<RutaPlanificadaDTO> rutasPlanificadas) {
    this.rutasPlanificadas = rutasPlanificadas;
  }

  public List<RequestPlanificacionDTO> getDonacionesNoPlanificadas() {
    return donacionesNoPlanificadas;
  }

  public void setDonacionesNoPlanificadas(List<RequestPlanificacionDTO> donacionesNoPlanificadas) {
    this.donacionesNoPlanificadas = donacionesNoPlanificadas;
  }

}
