package ar.edu.utn.frba.dds.necesidad.NecesidadDominio;

import ar.edu.utn.frba.dds.Bienes.Bien;

import java.util.ArrayList;
import java.util.List;

public class Peticion {
  private String subclase;
  private int cantidadRequerida;
  private int cantidadRecibida = 0;
  private List<String> donacionesAsignados = new ArrayList<>();


  public Peticion(String subclase, int cantidad) {
    this.subclase = subclase;
    this.cantidadRequerida = cantidad;
  }

  public boolean estaCubierta(){
    if (cantidadRecibida == cantidadRequerida) {
      return true;
    }else return false;
  }

  public String getSubclase(){
    return subclase;
  }

  public Integer getCantidadNecesitada(){
    return cantidadRequerida -cantidadRecibida;
  }

//   public String getSubclase() {
//     return subclase;
//   }

//   public int getCantidad() {
//     return cantidad;
//   }

//   public void setCantidad(int cantidad) {
//     this.cantidad = cantidad;
//   }

//   public void agregarBienesAsignados(List<Bien> bienes) {
//     bienesAsignados.addAll(bienes);
//   }

//   public List<Bien> getBienesAsignados() {
//     return bienesAsignados;
//   }
// }

  public void agregarCantidadRecibida (Integer cantidad, String idDonacion){
    this.cantidadRecibida += cantidad;
    this.donacionesAsignados.add(idDonacion);
  }
}