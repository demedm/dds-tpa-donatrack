package ar.edu.utn.frba.dds.scripts.dto;

public class CamionDto {
  private Long id;
  private String patente;
  private Double capacidadVolumen;
  private Double altura;
  private Double capacidadCarga;

  public CamionDto() {}

  public CamionDto(Long id, String patente, Double capacidadVolumen, Double capacidadCarga,
                   Double altura) {
    this.id = id;
    this.patente = patente;
    this.capacidadCarga = capacidadCarga;
    this.capacidadVolumen = capacidadVolumen;
    this.altura = altura;
  }

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

  public Long getId() {
    return id;
  }
}
