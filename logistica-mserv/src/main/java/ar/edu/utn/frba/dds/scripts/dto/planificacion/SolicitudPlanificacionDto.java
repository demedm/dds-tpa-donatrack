package ar.edu.utn.frba.dds.scripts.dto.planificacion;

import ar.edu.utn.frba.dds.scripts.dto.CamionDto;

import java.util.List;

public class SolicitudPlanificacionDto {
  private Long loteId;
  private String callbackUrl;
  private List<DonacionAPlanificarDto> donaciones;
  private List<CamionDisponibleDto> camiones;

  public SolicitudPlanificacionDto() {}

  public SolicitudPlanificacionDto(Long loteId, String callbackUrl,
                                   List<DonacionAPlanificarDto> donaciones,
                                   List<CamionDisponibleDto> camiones) {
    this.loteId = loteId;
    this.callbackUrl = callbackUrl;
    this.donaciones = donaciones;
    this.camiones = camiones;
  }

  public Long getLoteId() {
    return loteId;
  }

  public void setLoteId(Long loteId) {
    this.loteId = loteId;
  }

  public String getCallbackUrl() {
    return callbackUrl;
  }

  public void setCallbackUrl(String callbackUrl) {
    this.callbackUrl = callbackUrl;
  }
}
