package ar.edu.utn.frba.dds.dto;

public class PeticionDto {
    private Long id;
    private String subclase;
    private Double cantidadRequerida;
    private Double cantidadRecibida;
    private Double cantidadComprometida;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSubclase() { return subclase; }
    public void setSubclase(String subclase) { this.subclase = subclase; }
    public Double getCantidadRequerida() { return cantidadRequerida; }
    public void setCantidadRequerida(Double cantidadRequerida) { this.cantidadRequerida = cantidadRequerida; }
    public Double getCantidadRecibida() { return cantidadRecibida; }
    public void setCantidadRecibida(Double cantidadRecibida) { this.cantidadRecibida = cantidadRecibida; }
    public Double getCantidadComprometida() { return cantidadComprometida; }
    public void setCantidadComprometida(Double cantidadComprometida) { this.cantidadComprometida = cantidadComprometida; }
    public boolean isEstaCubierta() {
        if (cantidadRequerida == null || cantidadRecibida == null) return false;
        return cantidadRecibida >= cantidadRequerida;
    }
}