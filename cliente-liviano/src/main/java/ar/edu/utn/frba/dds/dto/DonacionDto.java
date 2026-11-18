package ar.edu.utn.frba.dds.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DonacionDto {

    private Long id;
    private String descripcionGeneral;
    private String fechaRecepcion;
    private long donanteId;
    private String donanteNombre;
    private List<SegmentadaDTO> segmentadas = List.of();

    public Long getId(){
        return id;
    }

    public String getDescripcionGeneral() {
        return descripcionGeneral;
    }

    public String getFechaRecepcion() {
        return fechaRecepcion;
    }

    public long getDonanteId() {
        return donanteId;
    }

    public String getDonanteNombre() {
        return donanteNombre;
    }

    public List<SegmentadaDTO> getSegmentadas() {
        return segmentadas;
    }
}
