package ar.edu.utn.frba.dds.model.necesidad;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.twilio.rest.api.v2010.account.availablephonenumbercountry.Local;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "necesidades")

public class Necesidad {
  @Id //La insatncio como primary key
  @GeneratedValue(strategy = GenerationType.IDENTITY) //Hace autoincremento

  private Long id;
  private String entidadId;

  @Enumerated(EnumType.STRING) //Guarda el enum como texto
  private TipoNecesidad tipo;

  private String estado = "en_preparacion";
  private String descripcion;
  private Integer diasRecurrencia;
  private LocalDate proximoVencimiento;

  @OneToMany(cascade = CascadeType.ALL) //Con cascada puedo ahcer que caundo guardo o modifico la nencesidad, guardo todas las peticioens que le agregue
  @JoinColumn(name = "necesidad_id") //El campo que vinculo a la necesidad. 

  public List<Peticion> peticiones = new ArrayList<>();
  //private List<String> idDonantesParticipantes = new ArrayList<>();

  public Necesidad() {}

  public Necesidad(String entidadId, TipoNecesidad tipo, String estado, String descripcion, Integer diasRecurrencia,List <Peticion> peticiones){
    this.entidadId = entidadId;
    this.tipo=tipo;
    this.estado = estado;
    this.descripcion = descripcion;
    this.diasRecurrencia=diasRecurrencia;
    this.proximoVencimiento= this.reiniciarPeriodo();
    this.peticiones = peticiones;
    }

  public Necesidad crearSiguienteRecurrencia(){
    List <Peticion> peticionesReiniciadas = new ArrayList<>();
    //Tengo que crear una nueva porque si no sigue guardando la conexion 
    for(Peticion p: this.peticiones){
      Peticion nuevaPeticion= new Peticion(p.getSubclase(),p.getCantidadRequerida());
      peticionesReiniciadas.add(nuevaPeticion);
    }

    return new Necesidad(
      this.entidadId,
      this.tipo,
      "en_preparacion",
      this.descripcion,
      this.diasRecurrencia,
      peticionesReiniciadas
    );
  }

  public String getEntidadId(){
    return this.entidadId;
  }

  public String getDescripcion(){
    return this.descripcion;
  }

  public Integer getDiasRecurrencia(){
    return this.diasRecurrencia;
  }

  public TipoNecesidad getTipo(){
    return this.tipo;
  }

  public String getEstado(){
    return this.estado;
  }

  public enum TipoNecesidad{
    NORMAL,
    RECURRENTE
  };

  public boolean estaVencida(){
    return this.proximoVencimiento.isBefore(LocalDate.now());
  }

  public long getDiasAvencer() {
    if (this.proximoVencimiento == null) {
        return 0;
    }
    return ChronoUnit.DAYS.between(LocalDate.now(), proximoVencimiento);
  }

  public LocalDate reiniciarPeriodo(){
    return LocalDate.now().plusDays(diasRecurrencia);
  }

  public void setProximoVencimiento(LocalDate fecha){
    this.proximoVencimiento=fecha;
  }

  public void agregarPeticion(Peticion peticion){
    this.peticiones.add(peticion);
  }

  public void setEntidadId(String entidadId) {
    this.entidadId = entidadId;
  }

  public void setTipo(TipoNecesidad tipo) {
    this.tipo = tipo;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public void setDiasRecurrencia(Integer diasRecurrencia) {
    this.diasRecurrencia = diasRecurrencia;
  }

  public void setEstado(String estado){
    this.estado = estado;
  }

  public Long getId() { return id; }
  
  public void setId(Long id) { this.id = id; }

  public List<Peticion> getPeticiones() {return this.peticiones;}

  public void setPeticiones(List <Peticion> peticiones){
    this.peticiones = peticiones;
  }
}
