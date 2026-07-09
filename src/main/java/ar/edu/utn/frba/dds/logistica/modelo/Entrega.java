package ar.edu.utn.frba.dds.logistica.modelo;


public class Entrega {
  private String direccion;
  private int entregaId; // id de la donacion
  private boolean visitado = false;

  public Entrega(String direccion, int entregaId) {
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

}
