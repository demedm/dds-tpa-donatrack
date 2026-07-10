package ar.edu.utn.frba.dds.model.necesidad;

import java.util.ArrayList;
import java.util.List;

public class Peticion {
  private String subclase;
  private int cantidadRequerida;
  private int cantidadRecibida = 0;
  private List<String> donacionesAsignados = new ArrayList<>();

  public Peticion() {}

    public void setSubclase(String subclase) {
    this.subclase = subclase;
  }

  public void setCantidadRequerida(int cantidadRequerida) {
    this.cantidadRequerida = cantidadRequerida;
  }

  public void setCantidadRecibida(int cantidadRecibida) {
    this.cantidadRecibida = cantidadRecibida;
  }

  public void setDonacionesAsignados(List<String> donacionesAsignados) {
    this.donacionesAsignados.addAll(donacionesAsignados);
  }

  public int getCantidadRecibida() {
    return cantidadRecibida;
  }

  public List<String> getDonacionesAsignados() {
    return donacionesAsignados;
  }

  public List<String> getDonacionesAsignadas() {
    return this.donacionesAsignados;
  }

    public Integer getCantidadRequerida(){
    return cantidadRequerida -cantidadRecibida;
  }

  public boolean estaCubierta(){
    if (cantidadRequerida == 0) {
      return true;
    }else return false;
  }

  public String getSubclase(){
    return subclase;
  }


  public void agregarCantidadRecibida (Integer cantidad, String idDonacion){
    this.cantidadRecibida += cantidad;
    this.donacionesAsignados.add(idDonacion);
  }
}