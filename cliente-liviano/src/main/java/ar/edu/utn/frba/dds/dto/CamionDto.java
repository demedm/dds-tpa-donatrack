package ar.edu.utn.frba.dds.dto;

public class CamionDto {
  private String patente;
  private Double capacidadVolumen;
  private Double altura;
  private Double capacidadCarga;

  public CamionDto() {}

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

}
