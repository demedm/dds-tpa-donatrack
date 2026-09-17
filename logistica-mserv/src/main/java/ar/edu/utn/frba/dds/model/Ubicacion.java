package ar.edu.utn.frba.dds.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Ubicacion {
  private Double latitud;
  private Double longitud;
  private LocalDateTime timestamp;

  public Ubicacion(Double latitud, Double longitud, LocalDateTime timestamp) {
    this.latitud = latitud;
    this.longitud = longitud;
    this.timestamp = timestamp;
  }

  public Ubicacion() {}

  public Double getLatitud() {
    return latitud;
  }

  public Double getLongitud() {
    return longitud;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

}