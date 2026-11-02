package ar.edu.utn.frba.dds.scripts.dto;

import ar.edu.utn.frba.dds.model.EstadoRuta;
import ar.edu.utn.frba.dds.model.Ruta;
import kotlin.UNINITIALIZED_VALUE;


public class RutaDto {
  private EstadoRuta estado;
  private Long choferId;
  private Long camionId;

  public RutaDto() {}

  public RutaDto(Ruta unaRuta) {
    this.estado = unaRuta.getEstado();
    this.choferId = unaRuta.getChofer().getId();
    this.camionId= unaRuta.getCamion().getId();

  }

  public EstadoRuta getEstado() {
    return estado;
  }

  public void setEstado(EstadoRuta estado) {
    this.estado = estado;
  }

  public Long getChoferId() {
    return choferId;
  }

  public void setChoferId(Long choferId) {
    this.choferId = choferId;
  }

  public Long getCamionId() {
    return camionId;
  }

  public void setCamionId(Long camionId) {
    this.camionId = camionId;
  }
}
