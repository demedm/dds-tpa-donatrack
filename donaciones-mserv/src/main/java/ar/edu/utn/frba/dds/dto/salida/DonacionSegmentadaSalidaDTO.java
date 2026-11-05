package ar.edu.utn.frba.dds.dto.salida;

import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import java.time.LocalDate;
import java.util.List;

public class DonacionSegmentadaSalidaDTO {

  private Long id;
  private String estado;
  private String categoria;
  private String subcategoria;
  private int cantidad;
  private String unidad;
  private Long entidadAsignadaId;
  private LocalDate fechaDeEntrega;
  private List<BienSalidaDTO> bienes;


  public static DonacionSegmentadaSalidaDTO desde(DonacionSegmentada segmentada) {
    DonacionSegmentadaSalidaDTO dto = new DonacionSegmentadaSalidaDTO();
    dto.id = segmentada.getId();
    dto.estado = segmentada.getEstadoActual().getNombre();

    Subcategoria sub = segmentada.getSubcategoria();
    dto.categoria = sub.getCategoria().name();
    dto.subcategoria = sub.getDescripcion();
    dto.unidad = sub.getUnidad().name();
    dto.cantidad = segmentada.getCantidad();

    dto.entidadAsignadaId = segmentada.getEntidadAsignadaId();
    dto.fechaDeEntrega = segmentada.getFechaDeEntrega();
    dto.bienes = segmentada.getBienes().stream()
        .map(BienSalidaDTO::desde)
        .toList();
    return dto;
  }

  public Long getId() { return id; }
  public String getEstado() { return estado; }
  public String getCategoria() { return categoria; }
  public String getSubcategoria() { return subcategoria; }
  public int getCantidad() { return cantidad; }
  public String getUnidad() { return unidad; }
  public Long getEntidadAsignadaId() { return entidadAsignadaId; }
  public LocalDate getFechaDeEntrega() { return fechaDeEntrega; }
  public List<BienSalidaDTO> getBienes() { return bienes; }


}
