package ar.edu.utn.frba.dds.logistica;
import ar.edu.utn.frba.dds.modelos.EstadoCamion;

import java.time.LocalDateTime;

public class CamionDashboardDTO {
  private String patente;
  private EstadoCamion estado;
  private Double latitud;
  private Double longitud;
  private LocalDateTime ultimaActualizacion;

  public String getPatente() { return patente; }
  public void setPatente(String patente) { this.patente = patente; }

  public EstadoCamion getEstado() { return estado; }
  public void setEstado(EstadoCamion estado) { this.estado = estado; }

  public Double getLatitud() { return latitud; }
  public void setLatitud(Double latitud) { this.latitud = latitud; }

  public Double getLongitud() { return longitud; }
  public void setLongitud(Double longitud) { this.longitud = longitud; }

  public LocalDateTime getUltimaActualizacion() { return ultimaActualizacion; }
  public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) { this.ultimaActualizacion = ultimaActualizacion; }
}