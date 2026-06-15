package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.medioscontacto.MedioContacto;

import java.time.LocalDateTime;

public class Notificacion {
  private String destinatario;
  private final String mensaje;
  private final LocalDateTime fechaHora;
  private EstadoNotificacion estado;

  public String getDestinatario() {
    return destinatario;
  }

  public String getMensaje() {
    return mensaje;
  }
  //Al menos un medio de contacto (en forma obligatoria correo electrónico y en forma opcional teléfono y/o WhatsApp).
  //A su vez, podrá determinar cuál de ellos será su medio de contacto predeterminado para recibir notificaciones del sistema.
  //Public EstadoNotificacion getEstado() { return estado; }

  public EstadoNotificacion getEstado() {
    return estado;
  }

  public Notificacion(String mensaje) {
    this.mensaje = mensaje;
    this.fechaHora = LocalDateTime.now();
    this.estado = EstadoNotificacion.PENDIENTE; // Nace pendiente
  }

  public void marcarComoCompletada() {
    this.estado = EstadoNotificacion.COMPLETADA;
  }

  public void marcarComoFallida() {
    this.estado = EstadoNotificacion.FALLIDA;
  }
}
