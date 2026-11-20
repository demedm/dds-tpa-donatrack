package ar.edu.utn.frba.dds.scripts.dto.planificacion;

import java.util.List;

public class RutaPlanificadaDto {
  private Long camionId;
  private List<DestinoDto> destinos;  // ids de las donaciones a entregar

  public RutaPlanificadaDto() {
    this.destinos = destinos == null ? List.of() : destinos;
  }

  public List<DestinoDto> getDestinos() {
    return destinos;
  }

  public void setDestinos(List<DestinoDto> destinos) {
    this.destinos = destinos;
  }

  public Long getCamionId() {
    return camionId;
  }

  public void setCamionId(Long camionId) {
    this.camionId = camionId;
  }
}
