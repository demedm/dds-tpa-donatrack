package ar.edu.utn.frba.dds.scripts.dto;

import java.util.List;

public class ResponsePlanificacionDto {
  private List<RutaPlanificadaDto> rutasPlanificadas;
  private List<RequestPlanificacionDto> donacionesNoPlanificadas;

  public ResponsePlanificacionDto() {}

  public List<RutaPlanificadaDto> getRutasPlanificadas() {
    return rutasPlanificadas;
  }

  public void setRutasPlanificadas(List<RutaPlanificadaDto> rutasPlanificadas) {
    this.rutasPlanificadas = rutasPlanificadas;
  }

  public List<RequestPlanificacionDto> getDonacionesNoPlanificadas() {
    return donacionesNoPlanificadas;
  }

  public void setDonacionesNoPlanificadas(List<RequestPlanificacionDto> donacionesNoPlanificadas) {
    this.donacionesNoPlanificadas = donacionesNoPlanificadas;
  }

}
