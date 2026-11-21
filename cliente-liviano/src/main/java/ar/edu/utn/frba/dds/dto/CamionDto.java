package ar.edu.utn.frba.dds.dto;

import ar.edu.utn.frba.dds.model.EstadoCamion;
import ar.edu.utn.frba.dds.vistas.Formato;
import com.camionesServiceGrpc.grpc.Camion;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CamionDto {
  private Long id;
  private String patente;
  private Double capacidadVolumen;
  private Double altura;
  private Double capacidadCarga;
  private String estado;
  private String nombreChofer;

  private boolean seleccionado;

  private static final DecimalFormat FMT =
      new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(new Locale("es", "AR")));

  public CamionDto() {}

  public CamionDto(Camion camion) {
    this.id = camion.getId();
    this.patente = camion.getPatente();
    this.nombreChofer = camion.getNombreChofer();
    this.capacidadCarga = camion.getCapacCarga();
    this.capacidadVolumen = camion.getCapacVolumen();
    this.altura = camion.getAltura();
    this.estado = camion.getEstado();
  }

  public static CamionDto fromProto(Camion camion) {
    return new CamionDto(camion);
  }

  public String getNombreChofer() {
    return nombreChofer.isBlank() ? "?" : nombreChofer;
  }

  public String getPatente() {
    return patente;
  }

  public Double getCapacidadVolumen() {
    return capacidadVolumen;
  }

  public Double getAltura() {
    return altura;
  }

  public Double getCapacidadCarga() {
    return capacidadCarga;
  }

  public Long getId() {
    return id;
  }

  public String getEstado() {
    return estado;
  }

  public String getCapacCargaTxt() { return FMT.format(capacidadCarga) + " kg"; }
  public String getCapacVolumenTxt() { return FMT.format(capacidadVolumen) + " m³"; }
  public String getAlturaTxt() { return FMT.format(altura) + " m"; }

  public String getCaracteristicas() {
    return getCapacCargaTxt() + " · " + getCapacVolumenTxt();
  }

  public String getEstadoTxt() {
    return Formato.estadoLogistica(estado);
  }

  public String getClaseEstado() {
    return Formato.badgeLogistica(estado);
  }

  public boolean isSeleccionado() {
    return seleccionado;
  }

  public void setSeleccionado(boolean seleccionado) {
    this.seleccionado = seleccionado;
  }
}
