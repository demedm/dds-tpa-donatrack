package ar.edu.utn.frba.dds.dto;

import java.util.ArrayList;
import java.util.List;

public class NecesidadDto {
    private Long id;
    private Long entidadId;
    private String tipo;
    private String estado;
    private String descripcion;
    private Integer diasRecurrencia;
    private List<PeticionDto> peticiones = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEntidadId() { return entidadId; }
    public void setEntidadId(Long entidadId) { this.entidadId = entidadId; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getDiasRecurrencia() { return diasRecurrencia; }
    public void setDiasRecurrencia(Integer diasRecurrencia) { this.diasRecurrencia = diasRecurrencia; }
    public List<PeticionDto> getPeticiones() { return peticiones; }
    public void setPeticiones(List<PeticionDto> peticiones) { this.peticiones = peticiones; }
    private String proximoVencimiento;
    private boolean vencida;

    public String getProximoVencimiento() { return proximoVencimiento; }
    public void setProximoVencimiento(String proximoVencimiento) { this.proximoVencimiento = proximoVencimiento; }

    public boolean isVencida() { return vencida; }
    public void setVencida(boolean vencida) { this.vencida = vencida; }

    public boolean isEsRecurrente() {
        return "RECURRENTE".equalsIgnoreCase(this.tipo);
    }

    public boolean isEstaVencida() {
        return this.vencida;
    }
}