package ar.edu.utn.frba.dds.model;

import java.time.LocalDateTime;

public class Ubicacion {
  private Double latitud;
  private Double longitud;
  private LocalDateTime timestamp;

  public Ubicacion(Double latitud, Double longitud, LocalDateTime timestamp) {
    this.latitud = latitud;
    this.longitud = longitud;
    this.timestamp = timestamp;
  }

  public Double getLatitud() { return latitud; }
  public Double getLongitud() { return longitud; }
  public LocalDateTime getTimestamp() { return timestamp; }
}