package ar.edu.utn.frba.dds.model.Donaciones;

import ar.edu.utn.frba.dds.model.Asignacion.AlgoritmoAsignacion;
import ar.edu.utn.frba.dds.model.Asignacion.Resultados;
import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Estado.EnDeposito;
import ar.edu.utn.frba.dds.model.Estado.EstadoDonacion;
import ar.edu.utn.frba.dds.model.Estado.RegistroCambioEstado;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;

import ar.edu.utn.frba.dds.model.Estado.EstadoDonacionConverter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ar.edu.utn.frba.dds.model.necesidad.Peticion;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;


import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Convert;


@Entity
@Table(name = "donaciones_segmentadas")

public class DonacionSegmentada {

  // ======================= ATRIBUTOS =======================

  // Identidad y de qué está hecha: la donación de la que sale y sus bienes
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "donacion_id")
  private Donacion donacion;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "segmentada_id")
  private List<Bien> bienes = new ArrayList<>();

  // Estados (patrón State) e historial de cambios
  @Convert(converter = EstadoDonacionConverter.class)
  @Column(name = "estado")
  @JsonIgnore
  private EstadoDonacion estadoActual = new EnDeposito();

  @ElementCollection
  @CollectionTable(name ="historial_estados",
      joinColumns = @JoinColumn(name = "donacion_segmentada_id"))
  @JsonIgnore
  private List<RegistroCambioEstado> historialEstados = new ArrayList<>();

  // Traslado y entrega
  private String justificacionFallo;
  private LocalDate fechaDeEntrega;

  // Asignación a una entidad beneficiaria
  private String entidadAsignadaId;

  @Transient
  private MedioContacto medioContactoEntidad;

  // Algoritmos de asignación (matchmaking): entidades propuestas
  @Transient
  private Resultados resultadosPropuestos;

  @ElementCollection
  @CollectionTable(name = "propuestas_asignacion",
      joinColumns = @JoinColumn(name = "donacion_segmentada_id"))
  @Column(name ="entidad_id")
  @JsonIgnore
  private List<String> idsEntidadesPropuestas = new ArrayList<>();


  // ======================= CONSTRUCTORES =======================

  // Constructor vacío que usa Hibernate para leer de la base
  protected DonacionSegmentada() {    this.historialEstados.add(new RegistroCambioEstado(this.estadoActual.getNombre(), LocalDateTime.now()));
  }

  // Constructor que usa Donacion al segmentar
  public DonacionSegmentada(Donacion donacion, List<Bien> bienes) {
    if (bienes == null || bienes.isEmpty()) {
      throw new IllegalArgumentException("Una donación segmentada necesita al menos un bien");
    }
    this.donacion = donacion;
    this.bienes = new ArrayList<>(bienes);
    this.estadoActual = new EnDeposito();
    this.historialEstados = new ArrayList<>();
    this.historialEstados.add(new RegistroCambioEstado(this.estadoActual.getNombre(), LocalDateTime.now()));
  }


  // ======================= DATOS DE LA SEGMENTADA =======================
  // Se calculan a partir de los bienes y de la donación

  public Long getId() {
    return id;
  }

  public List<Bien> getBienes() {
    return Collections.unmodifiableList(bienes);
  }

  public Integer getCantidad(){
    return bienes.stream().mapToInt(Bien::getCantidad).sum();
  }

  @JsonIgnore
  public Subcategoria getSubcategoria() {
    return bienes.get(0).getSubcategoria();
  }

  public Persona getDonante() {
    return donacion.getDonante();
  }


  // ======================= ESTADOS =======================
  // Cambio de estado, historial y consultas sobre el estado actual

  public void setEstado(EstadoDonacion nuevoEstado) {
    this.estadoActual = nuevoEstado;
    this.historialEstados.add(new RegistroCambioEstado(nuevoEstado.getNombre(), LocalDateTime.now()));
  }

  public EstadoDonacion getEstadoActual(){return  estadoActual;}

  @JsonIgnore
  public List<RegistroCambioEstado> getHistorialEstados() {
    return historialEstados;
  }

  @JsonIgnore
  public boolean estaAlmacen(){
    return estadoActual == null ||estadoActual instanceof EnDeposito;
  }

  // Traduce el texto que llega por HTTP a la transición correspondiente
  public void cambiarEstado(String nuevoEstado) {
    switch (nuevoEstado) {
      case "PENDIENTE" -> setEstado(new EnDeposito());
      case "ASIGNADA" -> asignar();
      case "PLANIFICADA", "LISTA_PARA_ENTREGAR" -> planificarRuta();
      case "EN_TRASLADO" -> iniciarTraslado();
      case "ENTREGADA" -> confirmarEntrega();
      case "NO_RECIBIDO", "NO_RECIBIDA", "FALLIDA" -> fallarEntrega(justificacionFallo);
      default -> throw new IllegalArgumentException("Estado desconocido: " + nuevoEstado);
    }
  }

  // Transiciones: cada una la resuelve el estado actual
  public void asignar() {
    estadoActual.asignar(this);
  }
  public void planificarRuta() {
    estadoActual.planificarRuta(this);
  }
  public void iniciarTraslado() {
    estadoActual.iniciarTraslado(this);
  }
  public void confirmarEntrega() {
    estadoActual.confirmarEntrega(this);
    fechaDeEntrega = LocalDate.now();
  }
  public void fallarEntrega(String justificacion) {
    estadoActual.fallarEntrega(this, justificacion);
  }
  public void vencer() {
    estadoActual.vencer(this);
  }


  // ======================= TRASLADO Y ENTREGA =======================

  public void setJustificacionFallo(String justificacion) {
    this.justificacionFallo = justificacion;
  }

  public LocalDate getFechaDeEntrega() {
    return this.fechaDeEntrega;
  }


  // ======================= ASIGNACIÓN A UNA ENTIDAD =======================

  public String getEntidadAsignadaId() {
    return entidadAsignadaId;
  }

  public void setEntidadAsignadaId(String entidadAsignadaId) {
    this.entidadAsignadaId = entidadAsignadaId;
  }

  public void asignarA(String entidadId) {
    asignar();
    this.entidadAsignadaId = entidadId;
  }

  public boolean puedeCubrir(Peticion peticion) {
    return estaAlmacen()
        && peticion.getSubclase().equalsIgnoreCase(getSubcategoria().getDescripcion())
        && peticion.getCantidadRecibida() < peticion.getCantidadRequerida();
  }


  // ======================= ALGORITMOS DE ASIGNACIÓN (MATCHMAKING) =======================
  // Busca entidades candidatas y guarda las propuestas

  public Resultados buscarCandidatas(List<EntidadBeneficiaria> entidades, List<AlgoritmoAsignacion> algoritmos) {
    if (!this.estaAlmacen()) {
      throw new IllegalStateException("Solo se puede asignar donaciones en estado EN_DEPOSITO");
    }

    List<List<EntidadBeneficiaria>> rankings = algoritmos.stream()
        .map(algoritmo -> algoritmo.obtenerRanking(this, entidades))
        .toList();

    List<EntidadBeneficiaria> coincidencias = rankings.get(0).stream()
        .filter(entidad -> rankings.stream().allMatch(ranking -> ranking.contains(entidad)))
        .toList();

    this.resultadosPropuestos = new Resultados(coincidencias, rankings);
    this.idsEntidadesPropuestas.clear();
    this.idsEntidadesPropuestas.addAll(
        resultadosPropuestos.entidadesPropuestas().stream()
            .map(EntidadBeneficiaria::getId)
            .toList());

    return this.resultadosPropuestos;

  }

  public Resultados getResultadosPropuestos(){
    return this.resultadosPropuestos;
  }

  public List<String> getIdsEntidadesPropuestas() {
    return idsEntidadesPropuestas;
  }


}