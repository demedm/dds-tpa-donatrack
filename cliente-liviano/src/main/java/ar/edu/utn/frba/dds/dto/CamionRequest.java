package ar.edu.utn.frba.dds.dto;

public class CamionRequest {
  private String patente;
  private Double capacidadVolumen;
  private Double altura;
  private Double capacidadCarga;
  private String estado;

  public CamionRequest() {}

  public String getPatente() {
    return patente;
  }

  public Double getCapacidadVolumen() {
    return capacidadVolumen;
  }

  public Double getAltura() {
    return altura;
  }

  public Double getCapacidadCarga() {
    return capacidadCarga;
  }

  public String getEstado() {
    return estado;
  }

  public void setPatente(String patente) {
    this.patente = patente;
  }

  public void setCapacidadVolumen(Double capacidadVolumen) {
    this.capacidadVolumen = capacidadVolumen;
  }

  public void setAltura(Double altura) {
    this.altura = altura;
  }

  public void setCapacidadCarga(Double capacidadCarga) {
    this.capacidadCarga = capacidadCarga;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }
}
