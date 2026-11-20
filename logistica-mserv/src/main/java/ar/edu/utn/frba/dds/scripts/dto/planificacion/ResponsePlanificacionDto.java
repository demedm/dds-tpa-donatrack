package ar.edu.utn.frba.dds.scripts.dto.planificacion;

import java.util.ArrayList;
import java.util.List;

public class ResponsePlanificacionDto {
  private Long loteId;
  private List<RutaPlanificadaDto> rutasPlanificadas = new ArrayList<>();
  private List<Long> donacionesNoPlanificadas = new ArrayList<>();

  public ResponsePlanificacionDto() {}

  public List<RutaPlanificadaDto> getRutasPlanificadas() {
    return rutasPlanificadas;
  }

  public void setRutasPlanificadas(List<RutaPlanificadaDto> rutasPlanificadas) {
    this.rutasPlanificadas = rutasPlanificadas;
  }

  public Long getLoteId() {
    return loteId;
  }

  public void setLoteId(Long loteId) {
    this.loteId = loteId;
  }

  public List<Long> getDonacionesNoPlanificadas() {
    return donacionesNoPlanificadas;
  }

  public void setDonacionesNoPlanificadas(List<Long> donacionesNoPlanificadas) {
    this.donacionesNoPlanificadas = donacionesNoPlanificadas;
  }
}
