package ar.edu.utn.frba.dds.scripts.dto.planificacion;

import ar.edu.utn.frba.dds.model.Camion;

public class CamionDisponibleDto {
  private Long id;
  private String patente;
  private Double capacidadVolumen;
  private Double capacidadCarga;
  private Double altura;

  public CamionDisponibleDto() {}

  public CamionDisponibleDto(Long id, String patente, Double capacidadVolumen,
                             Double capacidadCarga, Double altura) {
    this.id = id;
    this.patente = patente;
    this.capacidadVolumen = capacidadVolumen;
    this.capacidadCarga = capacidadCarga;
    this.altura = altura;
  }

  public static CamionDisponibleDto de(Camion c) {
    return new CamionDisponibleDto(
        c.getId(), c.getPatente(), c.getCapacidadVolumen(), c.getCapacidadCarga(), c.getAltura()
    );
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Double getCapacidadCarga() {
    return capacidadCarga;
  }

  public void setCapacidadCarga(Double capacidadCarga) {
    this.capacidadCarga = capacidadCarga;
  }

  public Double getAltura() {
    return altura;
  }

  public void setAltura(Double altura) {
    this.altura = altura;
  }
}
