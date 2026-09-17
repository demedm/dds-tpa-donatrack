package ar.edu.utn.frba.dds.scripts.dto;

import java.util.List;

public class SolicitudPlanificacionDto {
  private List<RequestPlanificacionDto> donaciones;
  private List<CamionDto> camionesDisponibles;
  private String urlCallback;

  public SolicitudPlanificacionDto(List<RequestPlanificacionDto> donaciones,
                                   List<CamionDto> camiones, String callbackUrl) {
    this.donaciones = donaciones;
    this.camionesDisponibles = camiones;
    this.urlCallback = callbackUrl;
  }

  public List<RequestPlanificacionDto> getDonaciones() {
    return donaciones;
  }

  public void setDonaciones(List<RequestPlanificacionDto> donaciones) {
    this.donaciones = donaciones;
  }

  public List<CamionDto> getCamionesDisponibles() {
    return camionesDisponibles;
  }

  public void setCamionesDisponibles(List<CamionDto> camionesDisponibles) {
    this.camionesDisponibles = camionesDisponibles;
  }

  public String getUrlCallback() {
    return urlCallback;
  }

  public void setUrlCallback(String urlCallback) {
    this.urlCallback = urlCallback;
  }
}
