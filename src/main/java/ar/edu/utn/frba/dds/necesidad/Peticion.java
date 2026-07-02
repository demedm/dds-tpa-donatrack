package ar.edu.utn.frba.dds.necesidad;

import ar.edu.utn.frba.dds.Bienes.Bien;
import ar.edu.utn.frba.dds.Bienes.Subcategoria;

import java.util.ArrayList;
import java.util.List;

public class Peticion {
  private Subcategoria subclase;
  private int cantidad;
  private List<Bien> bienesAsignados = new ArrayList<>();


  public Peticion(Subcategoria subclase, int cantidad) {
    this.subclase = subclase;
    this.cantidad = cantidad;
  }

  public Subcategoria getSubclase() {
    return subclase;
  }

  public int getCantidad() {
    return cantidad;
  }

  public void setCantidad(int cantidad) {
    this.cantidad = cantidad;
  }

  public void agregarBienesAsignados(List<Bien> bienes) {
    bienesAsignados.addAll(bienes);
  }

  public List<Bien> getBienesAsignados() {
    return bienesAsignados;
  }
}
