package ar.edu.utn.frba.dds.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DonacionDto {
    private Long id;
    private String descripcionGeneral;
    private String fechaRecepcion;
    private String donanteNombre;

    public Long getId() { return id; }
    public String getDescripcionGeneral() { return descripcionGeneral; }
    public String getFechaRecepcion() { return fechaRecepcion; }
    public String getDonanteNombre() { return donanteNombre; }
}