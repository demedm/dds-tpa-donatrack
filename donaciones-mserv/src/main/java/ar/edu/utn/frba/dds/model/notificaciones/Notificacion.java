package ar.edu.utn.frba.dds.model.notificaciones;

import java.time.LocalDateTime;
//Nace en pendiente

public class Notificacion {
  private String destinatario;
  private final String mensaje;
  private final LocalDateTime fechaHora;
  private EstadoNotificacion estado;
  private TipoEvento tipoEvento;

  public Notificacion(String mensaje) {
    this(mensaje, null);
  }

  public Notificacion(String mensaje, TipoEvento tipoEvento) {
    this.mensaje = mensaje;
    this.fechaHora = LocalDateTime.now();
    this.estado = EstadoNotificacion.PENDIENTE; // Nace pendiente
    this.tipoEvento = tipoEvento;
  }

  public void setDestinatario(String destinatario) {
    this.destinatario = destinatario;
  }

  public String getDestinatario() {
    return destinatario;
  }

  public String getMensaje() {
    return mensaje;
  }

  public LocalDateTime getFechaHora() {
    return fechaHora;
  }

  public EstadoNotificacion getEstado() {
    return estado;
  }

  public TipoEvento getTipoEvento() {
    return tipoEvento;
  }

  public void setTipoEvento(TipoEvento tipoEvento) {
    this.tipoEvento = tipoEvento;
  }

  public void marcarComoCompletada() {
    this.estado = EstadoNotificacion.COMPLETADA;
  }

  public void marcarComoFallida() {
    this.estado = EstadoNotificacion.FALLIDA;
  }
}