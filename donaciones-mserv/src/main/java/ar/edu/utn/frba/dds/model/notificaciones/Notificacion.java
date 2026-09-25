package ar.edu.utn.frba.dds.model.notificaciones;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
//Nace en pendiente

@Entity
@Table(name = "notificaciones")
public class Notificacion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "destinatario")
  private String destinatario;

  @Column(name = "mensaje", length = 1000)
  private String mensaje;

  @Column(name = "fecha_hora")
  private LocalDateTime fechaHora;

  @Enumerated(EnumType.STRING)
  @Column( name = "estado")
  private EstadoNotificacion estado;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_evento")
  private TipoEvento tipoEvento;

  protected Notificacion(){
  }

  public Notificacion(String mensaje) {
    this(mensaje, null);
  }

  public Notificacion(String mensaje, TipoEvento tipoEvento) {
    this.mensaje = mensaje;
    this.fechaHora = LocalDateTime.now();
    this.estado = EstadoNotificacion.PENDIENTE; // Nace pendiente
    this.tipoEvento = tipoEvento;
  }

  public Long getId() {
    return id;
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

  public boolean fueEnviada() {
    return estado == EstadoNotificacion.COMPLETADA;
  }

  public void marcarComoCompletada() {
    this.estado = EstadoNotificacion.COMPLETADA;
  }

  public void marcarComoFallida() {
    this.estado = EstadoNotificacion.FALLIDA;
  }
}