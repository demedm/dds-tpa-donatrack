package ar.edu.utn.frba.dds.dto.entrada;

import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.Categoria;
import ar.edu.utn.frba.dds.model.Bienes.CriterioSegmentacion;
import ar.edu.utn.frba.dds.model.Bienes.EstadoUso;
import ar.edu.utn.frba.dds.model.Bienes.PorEstado;
import ar.edu.utn.frba.dds.model.Bienes.PorVencimiento;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Bienes.UnidadMedida;

import java.math.BigDecimal;
import java.time.LocalDate;

// Lo que llega por el body para cada bien: datos planos que Jackson sí puede armar
public class BienDTO {
  private Categoria categoria;
  private String subcategoria;
  private UnidadMedida unidad;
  private String descripcion;
  private String foto;
  private BigDecimal cantidad;
  private LocalDate fechaVencimiento;   // si es perecedero
  private EstadoUso estadoUso;          // si es duradero

  public BienDTO() {}

  // El criterio sale de qué dato vino: vencimiento o estado de uso
  public CriterioSegmentacion criterio() {
    if (fechaVencimiento != null) {
      return new PorVencimiento(fechaVencimiento);
    }
    if (estadoUso != null) {
      return new PorEstado(estadoUso);
    }
    throw new IllegalArgumentException("El bien necesita fechaVencimiento o estadoUso");
  }

  public Bien aBien(Subcategoria sub) {
    return new Bien(sub, foto, descripcion, cantidad, criterio());
  }

  public Categoria getCategoria() { return categoria; }
  public void setCategoria(Categoria categoria) { this.categoria = categoria; }
  public String getSubcategoria() { return subcategoria; }
  public void setSubcategoria(String subcategoria) { this.subcategoria = subcategoria; }
  public UnidadMedida getUnidad() { return unidad; }
  public void setUnidad(UnidadMedida unidad) { this.unidad = unidad; }
  public String getDescripcion() { return descripcion; }
  public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
  public String getFoto() { return foto; }
  public void setFoto(String foto) { this.foto = foto; }
  public BigDecimal getCantidad() { return cantidad; }
  public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
  public LocalDate getFechaVencimiento() { return fechaVencimiento; }
  public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
  public EstadoUso getEstadoUso() { return estadoUso; }
  public void setEstadoUso(EstadoUso estadoUso) { this.estadoUso = estadoUso; }
}