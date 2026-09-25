package ar.edu.utn.frba.dds.model.Donaciones;

import ar.edu.utn.frba.dds.model.Asignacion.AlgoritmoAsignacion;
import ar.edu.utn.frba.dds.model.Asignacion.Resultados;
import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Estado.EnDeposito;
import ar.edu.utn.frba.dds.model.Estado.EstadoDonacion;
import ar.edu.utn.frba.dds.model.Estado.RegistroCambioEstado;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "donaciones_segmentadas")

public class DonacionSegmentada {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  private int cantidad;

  @Embedded
  private Subcategoria subcategoria;

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "bien_id")
  @JsonIgnore
  private Bien bienFiltrado;

  /*
  @Convert(converter = EstadoDonacion.class)
  @Column(name = "estado")
  */

  @Transient
  @JsonIgnore
  private EstadoDonacion estadoActual;

  /*
  @ElementCollection
  @CollectionTable(name ="historial_estados",
      joinColumns = @JoinColumn(name = "donacion_segmentada_id"))

  @OrderBy("fechaHora")
   */

  @Transient
  @JsonIgnore
  private List<RegistroCambioEstado> historialEstados = new ArrayList<>();


  private String justificacionFallo;
  private LocalDate fechaDeEntrega;
  private String donanteEmail;
  private String entidadAsignadaId;
  private Integer donanteId;

  @Transient
  private Resultados resultadosPropuestos;

  @Transient
  private MedioContacto medioContactoEntidad;

  @ElementCollection
  @CollectionTable(name = "propuestas_asignacion",
    joinColumns = @JoinColumn(name = "donacion_segmentada_id"))
  @Column(name ="entidad_id")
  @JsonIgnore
  private List<String> idsEntidadesPropuestas = new ArrayList<>();

  public List<String> getIdsEntidadesPropuestas() {
    return idsEntidadesPropuestas;
  }

  public String getDonanteEmail() {
    return donanteEmail;
  }

  public void setDonanteEmail(String donanteEmail) {
    this.donanteEmail = donanteEmail;
  }

  protected DonacionSegmentada() {}

  public DonacionSegmentada(Integer cantidad, Subcategoria subcategoria, Bien bienFiltrado) {
    this.cantidad = cantidad;
    this.subcategoria = subcategoria;
    this.bienFiltrado = bienFiltrado;
    this.estadoActual = new EnDeposito();
    this.historialEstados = new ArrayList<>();

  }

  public LocalDate getFechaDeEntrega() {
    return this.fechaDeEntrega;
  }

  public void setEstado(EstadoDonacion nuevoEstado) {
    this.estadoActual = nuevoEstado;
    this.historialEstados.add(new RegistroCambioEstado(nuevoEstado.getNombre(), LocalDateTime.now()));
  }
  @JsonIgnore
  public boolean estaAlmacen(){
    return estadoActual instanceof EnDeposito;
  }

  public void setJustificacionFallo(String justificacion) {
    this.justificacionFallo = justificacion;
  }

  public Integer getCantidad(){
    return cantidad;
  }

  public void setCantidad(Integer nuevaCantidad){
    cantidad = nuevaCantidad;
  }
  @JsonIgnore
  public Bien getBienFiltrado(){
    return bienFiltrado;
  }

  public Long getId() {
    return Id;
  }
  @JsonIgnore
  public List<RegistroCambioEstado> getHistorialEstados() {
    return historialEstados;
  }

  public String getEntidadAsignadaId() {
    return entidadAsignadaId;
  }

  public void setEntidadAsignadaId(String entidadAsignadaId) {
    this.entidadAsignadaId = entidadAsignadaId;
  }

  public void cambiarEstado(String nuevoEstado) {
    switch (nuevoEstado) {
      case "PENDIENTE" -> setEstado(new EnDeposito()); //creo que se debe crear la clase pendiente
      case "EN_TRASLADO" -> iniciarTraslado();
      case "ENTREGADA" -> confirmarEntrega();
      case "NO_RECIBIDO", "NO_RECIBIDA", "FALLIDA" -> fallarEntrega(justificacionFallo);
      default -> throw new IllegalArgumentException("Estado desconocido: " + nuevoEstado);
    }
  }

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
  @JsonIgnore
  public Subcategoria getSubcategoria() {
    return subcategoria;
  }

  //algoritmo de asignación

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




}
