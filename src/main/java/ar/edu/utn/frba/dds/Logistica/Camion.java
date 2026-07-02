package ar.edu.utn.frba.dds.Logistica;

import java.util.ArrayList;
import java.util.List;

public class Camion {
  private String patente;
  private int capacidadVolumen;
  private int altura;
  private int capacidadCarga;
  private EstadoCamion estado;
  private Ruta rutaActual = null;

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
    estado = EstadoCamion.EN_DEPOSITO;
    rutaActual.finalizarRuta();
    estado = EstadoCamion.DISPONIBLE;
  }

}
