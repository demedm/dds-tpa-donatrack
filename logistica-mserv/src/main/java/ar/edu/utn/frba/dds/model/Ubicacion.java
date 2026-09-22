package ar.edu.utn.frba.dds.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Ubicacion {
  @Id
  @GeneratedValue
  private Long id;

  @OneToOne
  private Camion camion;

  private Double latitud;
  private Double longitud;
  private LocalDateTime timestamp;

  public Ubicacion(Camion camion, Double latitud, Double longitud, LocalDateTime timestamp) {
    this.camion = camion;
    this.latitud = latitud;
    this.longitud = longitud;
    this.timestamp = timestamp;
  }

  public Ubicacion(Camion camion) {
    this.camion = camion;
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

  public Camion getCamion() {
    return camion;
  }

  // El timestap se actualiza a LocalDateTime.now()
  public void actualizarUbicacion(Double latitud, Double longitud) {
    // Solo permitimos actualizar si el camión está en ruta
    if (camion.getEstado() == EstadoCamion.REALIZANDO_ENTREGAS) {
      this.latitud = latitud;
      this.longitud = longitud;
      this.timestamp = LocalDateTime.now();
    }
  }

}