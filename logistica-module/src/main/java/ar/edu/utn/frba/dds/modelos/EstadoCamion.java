package ar.edu.utn.frba.dds.modelos;

import java.time.LocalDateTime;

public enum EstadoCamion {
  DISPONIBLE,
  EN_MANTENIMIENTO,
  RUTA_ASIGNADA,
  REALIZANDO_ENTREGAS;

  public static class CamionDashboardDTO {
    private String patente;
    private String estado;
    private Double latitud;
    private Double longitud;
    private LocalDateTime ultimaActualizacion;

    public String getPatente() { return patente; }
    public void setPatente(String patente) { this.patente = patente; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public LocalDateTime getUltimaActualizacion() { return ultimaActualizacion; }
    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) { this.ultimaActualizacion = ultimaActualizacion; }
  }
}
