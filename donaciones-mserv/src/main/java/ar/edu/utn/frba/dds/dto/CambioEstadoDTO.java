package ar.edu.utn.frba.dds.dto;

public class CambioEstadoDTO {

  private String nuevoEstado;
  private String motivoFalla;
  private boolean replanificable;

  // Datos para las notificaciones
  private String rutaId;
  private String urlMapa;        // inicio de ruta: enlace al mapa interactivo
  private String fechaHora;      // entrega exitosa: comprobante
  private String patenteCamion;  // entrega exitosa: camion responsable

  public CambioEstadoDTO() {}

  public String getNuevoEstado() { return nuevoEstado; }

  public void setNuevoEstado(String nuevoEstado) { this.nuevoEstado = nuevoEstado; }

  public String getMotivoFalla() { return motivoFalla; }

  public void setMotivoFalla(String motivoFalla) { this.motivoFalla = motivoFalla; }

  public boolean isReplanificable() { return replanificable; }

  public void setReplanificable(boolean replanificable) { this.replanificable = replanificable; }

  public String getRutaId() { return rutaId; }

  public void setRutaId(String rutaId) { this.rutaId = rutaId; }

  public String getUrlMapa() { return urlMapa; }

  public void setUrlMapa(String urlMapa) { this.urlMapa = urlMapa; }

  public String getFechaHora() { return fechaHora; }

  public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

  public String getPatenteCamion() { return patenteCamion; }

  public void setPatenteCamion(String patenteCamion) { this.patenteCamion = patenteCamion; }
}
