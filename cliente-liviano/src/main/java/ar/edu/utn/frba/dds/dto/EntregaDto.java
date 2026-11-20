package ar.edu.utn.frba.dds.dto;

import com.rutasServiceGrpc.grpc.Entrega;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class EntregaDto {
  private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  private final long id;
  private final String direccion;
  private final String fechaVencimiento;
  private final long donacionId;
  private final long beneficiarioId;
  private final String estado;

  private EntregaDto(Entrega e) {
    this.id = e.getId();
    this.direccion = e.getDireccion();
    this.fechaVencimiento = e.getFechaVencimiento();
    this.donacionId = e.getDonacionId();
    this.beneficiarioId = e.getBeneficiarioId();
    this.estado = e.getEstado();
  }

  public static EntregaDto fromProto(Entrega e) {
    return new EntregaDto(e);
  }

  public long getId() { return id; }
  public String getDireccion() { return direccion; }
  public long getDonacionId() { return donacionId; }
  public long getBeneficiarioId() { return beneficiarioId; }

  public String getEstadoTxt() {
    if (estado == null || estado.isBlank()) return "";
    String t = estado.replace('_', ' ').toLowerCase(new Locale("es", "AR"));
    return Character.toUpperCase(t.charAt(0)) + t.substring(1);
  }

  public String getVencimientoTxt() {
    if (fechaVencimiento == null || fechaVencimiento.isBlank()) return "—";
    try {
      return LocalDate.parse(fechaVencimiento).format(FECHA);
    } catch (DateTimeParseException ex) {
      return fechaVencimiento; // si el servidor manda otro formato, se muestra tal cual
    }
  }
}
