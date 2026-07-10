package ar.edu.utn.frba.dds.scripts.dto;

import ar.edu.utn.frba.dds.model.EstadoCamion;
import java.time.LocalDateTime;

public class CamionDashboardDTO {
  private String patente;
  private EstadoCamion estado;
  private Double latitud;
  private Double longitud;
  private LocalDateTime ultimaActualizacion;
  private Double porcentajeAvance;

  public String getPatente() {
    return patente;
  }

  public void setPatente(String patente) {
    this.patente = patente;
  }

  public EstadoCamion getEstado() {
    return estado;
  }

  public void setEstado(EstadoCamion estado) {
    this.estado = estado;
  }

  public Double getLatitud() {
    return latitud;
  }

  public void setLatitud(Double latitud) {
    this.latitud = latitud;
  }

  public Double getLongitud() {
    return longitud;
  }

  public void setLongitud(Double longitud) {
    this.longitud = longitud;
  }

  public LocalDateTime getUltimaActualizacion() {
    return ultimaActualizacion;
  }

  public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
    this.ultimaActualizacion = ultimaActualizacion;
  }

  public Double getPorcentajeAvance() {
    return porcentajeAvance;
  }

  public void setPorcentajeAvance(Double porcentajeAvance) {
    this.porcentajeAvance = porcentajeAvance;
  }
}