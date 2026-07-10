package ar.edu.utn.frba.dds.model;


import java.util.UUID;

public class Entrega {
  private String id; // id propio de la entrega
  private EstadoEntrega estado;
  private String direccion;
  private int donacionId; // id de la donacion
  private boolean visitado = false;

  public Entrega(String direccion, int donacionId) {
    this.estado = EstadoEntrega.PENDIENTE;
    this.donacionId = donacionId;
    this.direccion = direccion;
    id = UUID.randomUUID().toString();
  }

  public int getDonacionId() {
    return this.donacionId;
  }

  public void setVisitado(boolean visitado) {
    this.visitado = visitado;
  }

  public String getDireccion() {
    return this.direccion;
  }

  public boolean getVisitado() {
    return this.visitado;
  }

  public void marcarComoIniciada() {
    estado = EstadoEntrega.EN_TRASLADO;
  }

  public String getId() {
    return this.id;
  }

  public void marcarComoEntregada() {
    estado = EstadoEntrega.ENTREGADA;
    setVisitado(true);
  }

  public void regresarADeposito() {
    estado = EstadoEntrega.PENDIENTE;
  }

}
