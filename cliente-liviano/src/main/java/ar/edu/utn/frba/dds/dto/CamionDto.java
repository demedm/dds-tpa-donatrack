package ar.edu.utn.frba.dds.dto;

public class CamionDto {
  private Long id;
  private String patente;
  private Double capacidadVolumen;
  private Double altura;
  private Double capacidadCarga;
  private String estado;

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

  public Long getId() {
    return id;
  }

  public String getEstado() {
    return estado;
  }
}
