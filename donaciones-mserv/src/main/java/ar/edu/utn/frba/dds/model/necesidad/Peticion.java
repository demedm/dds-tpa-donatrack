package ar.edu.utn.frba.dds.model.necesidad;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;


@Entity
@Table(name = "peticiones")

public class Peticion {
  @Id //La insatncio como primary key
  @GeneratedValue(strategy = GenerationType.IDENTITY) //Hace autoincremento
  private Long id;
  private String subclase;
  private int cantidadRequerida;
  private int cantidadRecibida = 0;

  @ElementCollection //De momento habria un mcuhos a muchos, porque una peticion peude ser cubierta por muchas encesidades. Despues cambiar a un many-many, por ahroa mapea string siomple de id. 
    @CollectionTable(
        name = "peticion_donaciones", 
        joinColumns = @JoinColumn(name = "peticion_id")
    )
    @Column(name = "donacion_id")

  private List<String> donacionesAsignados = new ArrayList<>();

  public Peticion(){}; //JPA necesiota un constructor vacio

  public Peticion(String subclase, int cantidadRequerida) {
    this.cantidadRequerida=cantidadRequerida;
    this.subclase=subclase;
  }

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
    return cantidadRequerida;
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