package ar.edu.utn.frba.dds.dto;

import ar.edu.utn.frba.dds.model.EstadoCamion;
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
    if (estado == null || estado.isBlank()) return "";
    String texto = estado.replace('_', ' ').toLowerCase(new Locale("es", "AR"));
    return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
  }

  public String getClaseEstado() {
    String e = estado == null ? "" : estado.toUpperCase();
    if (e.contains(EstadoCamion.RUTA_ASIGNADA.name())) return "badge-ruta";
    if (e.contains(EstadoCamion.DISPONIBLE.name())) return "badge-disponible";
    if (e.contains(EstadoCamion.EN_MANTENIMIENTO.name())) return "badge-mantenimiento";
    if (e.contains(EstadoCamion.REALIZANDO_ENTREGAS.name())) return "badge-entregas";
    return "badge-disponible";
  }

  public boolean isSeleccionado() {
    return seleccionado;
  }

  public void setSeleccionado(boolean seleccionado) {
    this.seleccionado = seleccionado;
  }
}
