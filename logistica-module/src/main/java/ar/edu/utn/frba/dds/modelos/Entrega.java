package ar.edu.utn.frba.dds.modelos;

public class Entrega {
  private int donacionId;
  private String direccionEntrega;

  public Entrega(int ID, String direccion) {
    this.direccionEntrega = direccion;
    this.donacionId = ID;
  }

  public int getDonacionId() {
    return this.donacionId;
  }

  public String getDireccionEntrega() {
    return this.direccionEntrega;
  }

}