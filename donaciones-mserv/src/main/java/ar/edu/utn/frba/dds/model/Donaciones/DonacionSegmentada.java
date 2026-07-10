package ar.edu.utn.frba.dds.model.Donaciones;

import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Estado.EnDeposito;
import ar.edu.utn.frba.dds.model.Estado.EnTraslado;
import ar.edu.utn.frba.dds.model.Estado.Entregada;
import ar.edu.utn.frba.dds.model.Estado.EstadoDonacion;
import ar.edu.utn.frba.dds.model.Estado.RegistroCambioEstado;
import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DonacionSegmentada {
  private int id;
  private int cantidad;
  private Subcategoria subcategoria;
  private Bien bienFiltrado;
  private EstadoDonacion estadoActual;
  private List<RegistroCambioEstado> historialEstados;
  private String justificacionFallo;
  private LocalDate fechaDeEntrega;
  private MedioContacto medioContactoEntidad;
  private String donanteEmail;

  public String getDonanteEmail() {
    return donanteEmail;
  }

  public void setDonanteEmail(String donanteEmail) {
    this.donanteEmail = donanteEmail;
  }
  private String entidadAsignadaId;
  private Integer donanteId;

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

  public Bien getBienFiltrado(){
    return bienFiltrado;
  }

  public int getId() {
    return id;
  }

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

  public Subcategoria getSubcategoria() {
    return subcategoria;
  }
}
