package ar.edu.utn.frba.dds.model.necesidad;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

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

  public enum TipoNecesidad{
    NORMAL,
    RECURRENTE
  };

  public long getDiasAvencer() {
    if (this.proximoVencimiento == null) {
        return 0;
    }
    return ChronoUnit.DAYS.between(LocalDate.now(), proximoVencimiento);
  }

  public void reiniciarPeriodo(){
    this.proximoVencimiento=LocalDate.now().plusDays(diasRecurrencia);
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

  public Long getId() { return id; }
  
  public void setId(Long id) { this.id = id; }

  public List<Peticion> getPeticiones() {return this.peticiones;}

  public void setPeticiones(List <Peticion> peticiones){
    this.peticiones = peticiones;
  }
}
