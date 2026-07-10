package ar.edu.utn.frba.dds.scripts.dto;

public class CamionDTO {
  private String patente;
  private int capacidadVolumen;
  private int altura;
  private int capacidadCarga;

  public CamionDTO() {}

  public String getPatente() {
    return patente;
  }

  public void setPatente(String patente) {
    this.patente = patente;
  }

  public int getCapacidadVolumen() {
    return capacidadVolumen;
  }

  public void setCapacidadVolumen(int capacidadVolumen) {
    this.capacidadVolumen = capacidadVolumen;
  }

  public int getAltura() {
    return altura;
  }

  public void setAltura(int altura) {
    this.altura = altura;
  }

  public int getCapacidadCarga() {
    return capacidadCarga;
  }

  public void setCapacidadCarga(int capacidadCarga) {
    this.capacidadCarga = capacidadCarga;
  }
}
