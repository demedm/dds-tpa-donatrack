package ar.edu.utn.frba.dds.scripts.dto;

import ar.edu.utn.frba.dds.model.Camion;

import java.util.List;

public class SolicitudPlanificacionDTO {
  private List<RequestPlanificacionDTO> donaciones;
  private List<CamionDTO> camionesDisponibles;
  private String URLcallback;

  public SolicitudPlanificacionDTO(List<RequestPlanificacionDTO> donaciones,
                                   List<CamionDTO> camiones, String callbackUrl)
  {
    this.donaciones = donaciones;
    this.camionesDisponibles = camiones;
    this.URLcallback = callbackUrl;
  }

  public List<RequestPlanificacionDTO> getDonaciones() {
    return donaciones;
  }

  public void setDonaciones(List<RequestPlanificacionDTO> donaciones) {
    this.donaciones = donaciones;
  }

  public List<CamionDTO> getCamionesDisponibles() {
    return camionesDisponibles;
  }

  public void setCamionesDisponibles(List<CamionDTO> camionesDisponibles) {
    this.camionesDisponibles = camionesDisponibles;
  }

  public String getURLcallback() {
    return URLcallback;
  }

  public void setURLcallback(String URLcallback) {
    this.URLcallback = URLcallback;
  }
}
