package ar.edu.utn.frba.dds.modelos;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;


public class Camion {
  private String patente;
  private int capacidadVolumen;
  private int altura;
  private int capacidadCarga;
  private EstadoCamion estado;
  private Ruta rutaActual = null;
  private Ubicacion ubicacionActual;

  public EstadoCamion getEstado() {
    return estado;
  }

  public Camion(String patente) {
    this.estado = EstadoCamion.DISPONIBLE;
    this.patente = patente;
  }

  public String getPatente() { return this.patente; }

  public boolean asignarRuta(Ruta ruta) {
    if(estado == EstadoCamion.DISPONIBLE) {
      this.rutaActual = ruta;
      estado = EstadoCamion.RUTA_ASIGNADA;
      return true;
    }
    return false;
  }

  public void iniciarRuta() {
    estado = EstadoCamion.REALIZANDO_ENTREGAS;
    rutaActual.iniciarRuta();
  }

  public void visitarDestino(String direccion) {
    rutaActual.visitarParada(direccion);
  }

  public void regresarADeposito() {
    rutaActual.finalizarRuta();
    estado = EstadoCamion.DISPONIBLE;
  }

  public Ubicacion getUbicacionActual() {
    return ubicacionActual;
  }

  public void actualizarUbicacion(Double latitud, Double longitud) {
    // Solo permitimos actualizar si el camión está en ruta
    if (this.estado == EstadoCamion.REALIZANDO_ENTREGAS) {
      this.ubicacionActual = new Ubicacion(latitud, longitud, LocalDateTime.now());
    }
  }

}
