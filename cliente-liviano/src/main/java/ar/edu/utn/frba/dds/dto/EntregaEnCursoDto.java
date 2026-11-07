package ar.edu.utn.frba.dds.dto;

public class EntregaEnCursoDto {
  private Long id;
  private Long donacionId;
  private String direccion;
  private String estado;
  private Double latitudCamion;
  private Double longitudCamion;

  public EntregaEnCursoDto(Long id, Long donacionId, String direccion, String estado,
                           Double latitudCamion, Double longitudCamion) {
    this.id = id;
    this.direccion = direccion;
    this.donacionId = donacionId;
    this.estado = estado;
    this.latitudCamion = latitudCamion;
    this.longitudCamion = longitudCamion;
  }

  public Long getId() {
    return id;
  }

  public Long getDonacionId() {
    return donacionId;
  }

  public String getDireccion() {
    return direccion;
  }

  public String getEstado() {
    return estado;
  }

  public Double getLatitudCamion() {
    return latitudCamion;
  }

  public Double getLongitudCamion() {
    return longitudCamion;
  }
}
