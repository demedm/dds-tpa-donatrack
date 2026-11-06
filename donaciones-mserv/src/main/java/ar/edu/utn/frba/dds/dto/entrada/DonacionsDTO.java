package ar.edu.utn.frba.dds.dto.entrada;


import java.util.List;

public class DonacionsDTO {

  private String descripcionGeneral;
  private Long donanteId;            // antes: Persona donante
  private List<BienDTO> bienes;      // antes: List<Bien> bienes

  public DonacionsDTO() {

  }

  public String getDescripcionGeneral() {
    return descripcionGeneral;
  }

  public void setDescripcionGeneral(String descripcionGeneral) {
    this.descripcionGeneral = descripcionGeneral;
  }

  public List<BienDTO> getBienes() {
    return bienes;
  }

  public void setBienes(List<BienDTO> bienes) {
    this.bienes = bienes;
  }

  public Long getDonanteId() {
    return donanteId;
  }

  public void setDonanteId(Long donanteId) {
    this.donanteId = donanteId;
  }
}
