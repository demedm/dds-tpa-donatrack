package ar.edu.utn.frba.dds.dto.salida;

import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.PorEstado;
import ar.edu.utn.frba.dds.model.Bienes.PorVencimiento;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BienSalidaDTO {

  private String descripcion;
  private String foto;
  private BigDecimal cantidad;
  private String unidad;
  private String categoria;
  private String subcategoria;
  private LocalDate fechaVencimiento;
  private String estadoUso;

  public static BienSalidaDTO desde(Bien bien) {
    BienSalidaDTO dto = new BienSalidaDTO();
    dto.descripcion = bien.getDescripcion();
    dto.foto = bien.getFoto();
    dto.cantidad = bien.getCantidad();

    Subcategoria sub = bien.getSubcategoria();
    dto.categoria = sub.getCategoria().name();
    dto.subcategoria = sub.getDescripcion();
    dto.unidad = sub.getUnidad().name();

    if (bien.getCriterio() instanceof PorVencimiento vencimiento) {
      dto.fechaVencimiento = vencimiento.getFechaVencimiento();
    } else if (bien.getCriterio() instanceof PorEstado estado) {
      dto.estadoUso = estado.getEstado().name();
    }
    return dto;
  }

  public String getDescripcion() { return descripcion; }
  public String getFoto() { return foto; }
  public BigDecimal getCantidad() { return cantidad; }
  public String getUnidad() { return unidad; }
  public String getCategoria() { return categoria; }
  public String getSubcategoria() { return subcategoria; }
  public LocalDate getFechaVencimiento() { return fechaVencimiento; }
  public String getEstadoUso() { return estadoUso; }

}
