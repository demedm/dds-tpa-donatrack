package ar.edu.utn.frba.dds.scripts.dto;

import java.util.List;

public class RutaPlanificadaDto {
  private String patenteCamion;
  private List<DestinoDto> destinos;  // ids de las donaciones a entregar

  public RutaPlanificadaDto() {}

  public String getPatenteCamion() {
    return patenteCamion;
  }

  public void setPatenteCamion(String patenteCamion) {
    this.patenteCamion = patenteCamion;
  }

  public List<DestinoDto> getDestinos() {
    return destinos;
  }

  public void setDestinos(List<DestinoDto> destinos) {
    this.destinos = destinos;
  }

}
