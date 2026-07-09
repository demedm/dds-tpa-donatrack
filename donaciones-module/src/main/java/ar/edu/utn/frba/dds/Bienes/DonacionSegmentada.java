package ar.edu.utn.frba.dds.Bienes;

import ar.edu.utn.frba.dds.Estado.EnDeposito;
import ar.edu.utn.frba.dds.Estado.EstadoDonacion;
import ar.edu.utn.frba.dds.Estado.RegistroCambioEstado;
import ar.edu.utn.frba.dds.medioscontacto.MedioContacto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DonacionSegmentada {
  private int cantidad;
  private Subcategoria subcategoria;
  private Bien bienFiltrado;
  private EstadoDonacion estadoActual;
  private List<RegistroCambioEstado> historialEstados;
  private String justificacionFallo;
  private LocalDate fechaDeEntrega;
  private MedioContacto medioContactoEntidad;


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

  public List<RegistroCambioEstado> getHistorialEstados() {
    return historialEstados;}

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
  }
  public void fallarEntrega(String justificacion) {
    estadoActual.fallarEntrega(this, justificacion);
  }
  public void vencer() {
    estadoActual.vencer(this);
  }
  public String getNombreEstado() {
    return estadoActual.getNombre();
  }

  public Subcategoria getSubcategoria() {
    return subcategoria;
  }
}
