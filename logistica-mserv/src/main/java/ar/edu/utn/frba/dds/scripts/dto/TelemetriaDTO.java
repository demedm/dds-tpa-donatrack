package ar.edu.utn.frba.dds.scripts.dto;
import java.time.LocalDateTime;


public class TelemetriaDTO {
  private String patente;
  private Double latitud;
  private Double longitud;
  private Double velocidad;
  private LocalDateTime timestampReporte;

  // Getters y Setters
  public String getPatente() { return patente; }
  public void setPatente(String patente) { this.patente = patente; }
  public Double getLatitud() { return latitud; }
  public void setLatitud(Double latitud) { this.latitud = latitud; }
  public Double getLongitud() { return longitud; }
  public void setLongitud(Double longitud) { this.longitud = longitud; }
  public Double getVelocidad() { return velocidad; }
  public void setVelocidad(Double velocidad) { this.velocidad = velocidad; }
  public LocalDateTime getTimestampReporte() { return timestampReporte; }
  public void setTimestampReporte(LocalDateTime timestampReporte) {
    this.timestampReporte = timestampReporte;
   }
}