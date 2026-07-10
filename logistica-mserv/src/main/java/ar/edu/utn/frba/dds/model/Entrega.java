package ar.edu.utn.frba.dds.model;


public class Entrega {
  private EstadoEntrega estado;
  private String direccion;
  private int entregaId; // id de la donacion
  private boolean visitado = false;

  public Entrega(String direccion, int entregaId) {
    this.estado = EstadoEntrega.PENDIENTE;
    this.entregaId = entregaId;
    this.direccion = direccion;
  }

  public int getEntregaId() {
    return this.entregaId;
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

  public void marcarComoEntregada() {
    estado = EstadoEntrega.ENTREGADA;
    setVisitado(true);
  }

  public void regresarADeposito() {
    estado = EstadoEntrega.PENDIENTE;
  }

}
