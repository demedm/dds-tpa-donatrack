package ar.edu.utn.frba.dds.model.necesidad;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "necesidades")

public class Necesidad {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)

  private Long id;
  private Long entidadId;

  @Enumerated(EnumType.STRING)
  private TipoNecesidad tipo;

  private String estado = "en_preparacion";
  private String descripcion;
  private Integer diasRecurrencia;
  private LocalDate proximoVencimiento;

  @ElementCollection
  @CollectionTable(name = "necesidad_adjuntos", joinColumns = @JoinColumn(name = "necesidad_id"))
  @Column(name = "ruta_adjunto")
  private List<String> adjuntos = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "necesidad_id")

  public List<Peticion> peticiones = new ArrayList<>();
  //private List<String> idDonantesParticipantes = new ArrayList<>();

  public Necesidad() {}

  public Necesidad(Long entidadId, TipoNecesidad tipo, String estado, String descripcion, Integer diasRecurrencia,List <Peticion> peticiones){
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

  public Long getEntidadId(){
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

  // Cubierta cuando todas sus peticiones recibieron lo que pedían
  public boolean estaCubierta() {
    return !peticiones.isEmpty() && peticiones.stream().allMatch(Peticion::estaCubierta);
  }

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
    if (this.peticiones == null) {
      this.peticiones = new ArrayList<>();
    }
    this.peticiones.add(peticion);
    peticion.setNecesidad(this);
  }

  public Boolean esRecurrente(){
    return  this.tipo == TipoNecesidad.RECURRENTE;
  }

  public void setEntidadId(Long entidadId) {
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

  public List<String> getAdjuntos() {
    return adjuntos;
  }

  public void agregarAdjunto(String ruta) {
    this.adjuntos.add(ruta);
  }

  public boolean isTieneAdjuntos() {
    return !adjuntos.isEmpty();
  }
}
