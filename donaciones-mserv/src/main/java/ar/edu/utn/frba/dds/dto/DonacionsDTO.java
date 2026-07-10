package ar.edu.utn.frba.dds.dto;

import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.donantes.Persona;

import java.util.List;

public class DonacionsDTO {

  private String descripcionGeneral;
  private Persona donante;
  private List<Bien> bienes;
  private Integer id;

  public DonacionsDTO() {

  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getDescripcionGeneral() {
    return descripcionGeneral;
  }

  public void setDescripcionGeneral(String descripcionGeneral) {
    this.descripcionGeneral = descripcionGeneral;
  }

  public Persona getDonante() {
    return donante;
  }

  public void setDonante(Persona donante) {
    this.donante = donante;
  }

  public List<Bien> getBienes() {
    return bienes;
  }

  public void setBienes(List<Bien> bienes) {
    this.bienes = bienes;
  }
}
