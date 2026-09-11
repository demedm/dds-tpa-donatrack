package ar.edu.utn.frba.dds.model;

import ar.edu.utn.frba.dds.model.accionesentregas.AccionesSobreEntregas;
import ar.edu.utn.frba.dds.model.accionesentregas.Notificar;
import ar.edu.utn.frba.dds.model.accionesentregas.NotificarAdmins;
import ar.edu.utn.frba.dds.model.fallaentrega.EntregaVencida;
import ar.edu.utn.frba.dds.model.fallaentrega.MotivoFallo;
import ar.edu.utn.frba.dds.model.fallaentrega.NoRecepcionada;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Entrega {
  @Id
  @GeneratedValue
  private Long id1;
  private String id;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado_entrega")
  private EstadoEntrega estado;

  private String direccion;
  private Integer donacionId;
  private LocalDate fechaVencimiento;
  private boolean entregado = false;

  @Transient
  private MotivoFallo motivoFallo;

  private String foto;

  @Transient
  private List<AccionesSobreEntregas> accionesSobreEntregas = new ArrayList<>();

  public Entrega(String direccion, int idDonacion) {
    this.estado = EstadoEntrega.PENDIENTE;
    this.donacionId = idDonacion;
    this.direccion = direccion;
    this.id = UUID.randomUUID().toString();

    // Lógica nueva del equipo fusionada correctamente
    agregarAccionEntregas(new NotificarAdmins());
    agregarAccionEntregas(new Notificar());
  }

  public Entrega() {}

  public LocalDate getFechaVencimiento() {
    return fechaVencimiento;
  }

  public void setFechaVencimiento(LocalDate fechaVencimiento) {
    this.fechaVencimiento = fechaVencimiento;
  }

  public int getDonacionId() {
    return this.donacionId;
  }

  public void setEntregado(boolean entregado) {
    this.entregado = entregado;
  }

  public String getDireccion() {
    return this.direccion;
  }

  public boolean getEntregado() {
    return this.entregado;
  }

  public Long getId1() {
    return id1;
  }

  public void setId1(Long id1) {
    this.id1 = id1;
  }

  public String getId() {
    return this.id;
  }

  public void marcarComoIniciada() {
    estado = EstadoEntrega.EN_TRASLADO;
    accionesSobreEntregas.forEach(accion ->
        accion.notificarInicioRuta(this));
  }

  public void marcarComoEntregada() {
    entregado = true;
    estado = EstadoEntrega.ENTREGADA;
  }

  public void confirmarEntrega(String urlFoto) {
    setFoto(urlFoto);
  }

  public void marcarComoFallida(MotivoFallo motivo) {
    estado = EstadoEntrega.FALLIDA;
    setMotivoFallo(motivo);
    accionesSobreEntregas.forEach(accion ->
        accion.notificarFalloEntrega(this));
  }

  public void marcarRegreso() {
    estado = EstadoEntrega.PENDIENTE;
  }

  public void marcarComoNoRecepcionada() {
    marcarComoFallida(new NoRecepcionada());
  }

  public boolean estaVencida() {
    if (fechaVencimiento != null && fechaVencimiento.isBefore(LocalDate.now())) {
      marcarComoFallida(new EntregaVencida());
      return true;
    }
    return false;
  }

  public MotivoFallo getMotivoFallo() {
    return motivoFallo;
  }

  public void setMotivoFallo(MotivoFallo motivoFallo) {
    this.motivoFallo = motivoFallo;
  }

  public void agregarAccionEntregas(AccionesSobreEntregas accion) {
    accionesSobreEntregas.add(accion);
  }

  public String getFoto() {
    return foto;
  }

  public void setFoto(String foto) {
    this.foto = foto;
  }

  public EstadoEntrega getEstado() {
    return estado;
  }

  public void setEstado(EstadoEntrega estado) {
    this.estado = estado;
  }

}