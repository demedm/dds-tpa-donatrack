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



}
