package ar.edu.utn.frba.dds.scripts.dto;

import java.util.List;

public class RutaPlanificadaDTO {
  private String patenteCamion;
  private List<DestinoDTO> destinos;  // ids de las donaciones a entregar

  public RutaPlanificadaDTO() {}

  public String getPatenteCamion() {
    return patenteCamion;
  }

  public void setPatenteCamion(String patenteCamion) {
    this.patenteCamion = patenteCamion;
  }

  public List<DestinoDTO> getDestinos() {
    return destinos;
  }

  public void setDestinos(List<DestinoDTO> destinos) {
    this.destinos = destinos;
  }

}
