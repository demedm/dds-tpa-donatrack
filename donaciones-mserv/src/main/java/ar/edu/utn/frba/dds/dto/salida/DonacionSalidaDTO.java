package ar.edu.utn.frba.dds.dto.salida;

import ar.edu.utn.frba.dds.model.Donaciones.Donacion;

import java.time.LocalDate;
import java.util.List;

public class DonacionSalidaDTO {

  private Long id;
  private String descripcionGeneral;
  private LocalDate fechaRecepcion;
  private Long donanteId;
  private String donanteNombre;
  private List<DonacionSegmentadaSalidaDTO> segmentadas;

  public static DonacionSalidaDTO desde(Donacion donacion) {
    DonacionSalidaDTO dto = new DonacionSalidaDTO();
    dto.id = donacion.getId();
    dto.descripcionGeneral = donacion.getDescripcionGeneral();
    dto.fechaRecepcion = donacion.getFechaRecepcion();
    dto.donanteId = donacion.getDonante().getId();
    dto.donanteNombre = donacion.getDonante().getNombreIdentificador();
    dto.segmentadas = donacion.getSegmentadas().stream()
        .map(DonacionSegmentadaSalidaDTO::desde)
        .toList();
    return dto;
  }

  public Long getId() { return id; }
  public String getDescripcionGeneral() { return descripcionGeneral; }
  public LocalDate getFechaRecepcion() { return fechaRecepcion; }
  public Long getDonanteId() { return donanteId; }
  public String getDonanteNombre() { return donanteNombre; }
  public List<DonacionSegmentadaSalidaDTO> getSegmentadas() { return segmentadas; }
  
}
