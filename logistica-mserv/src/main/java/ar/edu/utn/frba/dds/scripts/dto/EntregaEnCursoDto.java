package ar.edu.utn.frba.dds.scripts.dto;

import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.EstadoEntrega;
import ar.edu.utn.frba.dds.model.Ubicacion;

public class EntregaEnCursoDto {
    private Long id;
    private Long donacionId;
    private String direccion;
    private EstadoEntrega estado;
    private Double latitudCamion;
    private Double longitudCamion;

    public EntregaEnCursoDto(Entrega entrega, Ubicacion ubicacion) {
        this.id = entrega.getId();
        this.donacionId = entrega.getDonacionId();
        this.direccion = entrega.getDireccion();
        this.estado = entrega.getEstado();

        if (ubicacion != null) {
            this.latitudCamion = ubicacion.getLatitud();
            this.longitudCamion = ubicacion.getLongitud();
        }
    }

    // Getters para que Jackson pueda serializar a JSON
    public Long getId() { return id; }
    public Long getDonacionId() { return donacionId; }
    public String getDireccion() { return direccion; }
    public EstadoEntrega getEstado() { return estado; }
    public Double getLatitudCamion() { return latitudCamion; }
    public Double getLongitudCamion() { return longitudCamion; }
}