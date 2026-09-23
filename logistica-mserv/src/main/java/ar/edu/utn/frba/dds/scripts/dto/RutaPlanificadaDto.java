package ar.edu.utn.frba.dds.scripts.dto;

import ar.edu.utn.frba.dds.model.Camion;

import java.util.List;

public class RutaPlanificadaDto {
  private Camion camion;
  private List<DestinoDto> destinos;  // ids de las donaciones a entregar

  public RutaPlanificadaDto() {}

  public Camion getCamion() {
    return camion;
  }

  public void setCamion(Camion camion) {
    this.camion = camion;
  }

  public List<DestinoDto> getDestinos() {
    return destinos;
  }

  public void setDestinos(List<DestinoDto> destinos) {
    this.destinos = destinos;
  }

}
