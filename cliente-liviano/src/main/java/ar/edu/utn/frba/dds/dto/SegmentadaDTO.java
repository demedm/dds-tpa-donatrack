package ar.edu.utn.frba.dds.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SegmentadaDTO {

    private Long id;
    private String estado;
    private String categoria;
    private String subcategoria;
    private BigDecimal cantidad;
    private String unidad;
    private Long entidadAsignadaId;

    private String vencimiento;
    private String estadoUso;


    public Long getId() { return id; }
    public String getEstado() { return estado; }
    public String getCategoria() { return categoria; }
    public String getSubcategoria() { return subcategoria; }
    public BigDecimal getCantidad() { return cantidad; }
    public String getUnidad() { return unidad; }
    public Long getEntidadAsignadaId() { return entidadAsignadaId; }
    public String getVencimiento() { return vencimiento; }
    public String getEstadoUso() { return estadoUso; }

}
