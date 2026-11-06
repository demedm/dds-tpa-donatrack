package ar.edu.utn.frba.dds.model.Donaciones;

import ar.edu.utn.frba.dds.model.Asignacion.AlgoritmoAsignacion;
import ar.edu.utn.frba.dds.model.Asignacion.EjecucionAlgoritmo;
import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Estado.EnDeposito;
import ar.edu.utn.frba.dds.model.Estado.EstadoDonacion;
import ar.edu.utn.frba.dds.model.Estado.RegistroCambioEstado;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;

import ar.edu.utn.frba.dds.model.Estado.EstadoDonacionConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ar.edu.utn.frba.dds.model.necesidad.Peticion;


import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
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
  private EstadoDonacion estadoActual = new EnDeposito();

  @ElementCollection
  @CollectionTable(name ="historial_estados",
      joinColumns = @JoinColumn(name = "donacion_segmentada_id"))

  private List<RegistroCambioEstado> historialEstados = new ArrayList<>();

  // Traslado y entrega
  private String justificacionFallo;
  private LocalDate fechaDeEntrega;

  // Asignación a una entidad beneficiaria
  private Long entidadAsignadaId;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "donacion_segmentada_id")
  @OrderColumn(name = "orden")
  private List<EjecucionAlgoritmo> ejecuciones = new ArrayList<>();

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

  public BigDecimal getCantidad(){
    return bienes.stream().map(Bien::getCantidad).reduce(BigDecimal.ZERO,BigDecimal::add);
  }

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

  public List<RegistroCambioEstado> getHistorialEstados() {
    return historialEstados;
  }

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
      case "VENCIDA" -> vencer();
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

  public Long getEntidadAsignadaId() {
    return entidadAsignadaId;
  }

  public void setEntidadAsignadaId(Long entidadAsignadaId) {
    this.entidadAsignadaId = entidadAsignadaId;
  }

  public void asignarA(Long entidadId) {
    asignar();
    this.entidadAsignadaId = entidadId;
  }

  public boolean puedeCubrir(Peticion peticion) {
    return estaAlmacen()
        && peticion.getSubclase().equalsIgnoreCase(getSubcategoria().getDescripcion())
        && peticion.getCantidadRecibida().compareTo(peticion.getCantidadRequerida()) < 0;
  }


  // ======================= ALGORITMOS DE ASIGNACIÓN (MATCHMAKING) =======================
  // Busca entidades candidatas y guarda las propuestas

  public void buscarCandidatas(List<EntidadBeneficiaria> entidades, List<AlgoritmoAsignacion> algoritmos) {
    if (!this.estaAlmacen()) {
      throw new IllegalStateException("Solo se puede asignar donaciones en estado EN_DEPOSITO");
    }

    this.ejecuciones.clear();
    for(AlgoritmoAsignacion algoritmo : algoritmos) {
      List<Long> ranking = algoritmo.obtenerRanking(this,entidades).stream()
          .map(EntidadBeneficiaria::getId)
          .toList();
      this.ejecuciones.add(new EjecucionAlgoritmo(algoritmo.nombre(), ranking));
    }

  }


  public List<EjecucionAlgoritmo> getEjecuciones() {
    return Collections.unmodifiableList(this.ejecuciones);
  }

  public List<Long> getCoincidencias(){
    if(ejecuciones.isEmpty()){
      return List.of();
    }
    return ejecuciones.get(0).getIdsEntidades().stream()
        .filter(entidadId -> ejecuciones.stream().allMatch(ejecucion ->
            ejecucion.propone(entidadId)))
        .toList();
  }

  public boolean huboCoincidencias() {
    return !getCoincidencias().isEmpty();
  }

  // Si hubo coincidencias, esas; si no, todas las entidades de todas las ejecuciones (sin repetir)
  public List<Long> getIdsEntidadesPropuestas() {
    if (huboCoincidencias()) {
      return getCoincidencias();
    }
    return ejecuciones.stream()
        .flatMap(ejecucion -> ejecucion.getIdsEntidades().stream())
        .distinct()
        .toList();
  }


}