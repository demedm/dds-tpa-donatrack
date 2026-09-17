package ar.edu.utn.frba.dds.scripts.dto;

public class CamionDto {
  private String patente;
  private Double capacidadVolumen;
  private Double altura;
  private Double capacidadCarga;

  public CamionDto() {}

  public String getPatente() {
    return patente;
  }

  public void setPatente(String patente) {
    this.patente = patente;
  }

  public Double getCapacidadVolumen() {
    return capacidadVolumen;
  }

  public void setCapacidadVolumen(Double capacidadVolumen) {
    this.capacidadVolumen = capacidadVolumen;
  }

  public Double getAltura() {
    return altura;
  }

  public void setAltura(Double altura) {
    this.altura = altura;
  }

  public Double getCapacidadCarga() {
    return capacidadCarga;
  }

  public void setCapacidadCarga(Double capacidadCarga) {
    this.capacidadCarga = capacidadCarga;
  }
}
