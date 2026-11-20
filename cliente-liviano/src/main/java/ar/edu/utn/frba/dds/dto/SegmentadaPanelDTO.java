package ar.edu.utn.frba.dds.dto;

public class SegmentadaPanelDTO {
  private Long id;
  private Long donacionId;
  private String fechaRecepcion;
  private String estado;
  private String categoria;
  private String subcategoria;
  private String cantidad;
  private String unidad;
  private String entidadAsignada;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getDonacionId() {
    return donacionId;
  }

  public void setDonacionId(Long donacionId) {
    this.donacionId = donacionId;
  }

  public String getFechaRecepcion() {
    return fechaRecepcion;
  }

  public void setFechaRecepcion(String fechaRecepcion) {
    this.fechaRecepcion = fechaRecepcion;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public String getSubcategoria() {
    return subcategoria;
  }

  public void setSubcategoria(String subcategoria) {
    this.subcategoria = subcategoria;
  }

  public String getCantidad() {
    return cantidad;
  }

  public void setCantidad(String cantidad) {
    this.cantidad = cantidad;
  }

  public String getUnidad() {
    return unidad;
  }

  public void setUnidad(String unidad) {
    this.unidad = unidad;
  }

  public String getEntidadAsignada() {
    return entidadAsignada;
  }

  public void setEntidadAsignada(String entidadAsignada) {
    this.entidadAsignada = entidadAsignada;
  }
}
