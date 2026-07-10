package ar.edu.utn.frba.dds.model;


import ar.edu.utn.frba.dds.model.accionesentregas.AccionesSobreEntregas;
import ar.edu.utn.frba.dds.model.accionesentregas.NotificarAdmins;
import ar.edu.utn.frba.dds.model.accionesentregas.NotificarDonadorYDonante;
import ar.edu.utn.frba.dds.model.fallaentrega.EntregaVencida;
import ar.edu.utn.frba.dds.model.fallaentrega.ImprevistoLogistico;
import ar.edu.utn.frba.dds.model.fallaentrega.MotivoFallo;
import ar.edu.utn.frba.dds.model.fallaentrega.NoRecepcionada;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import ar.edu.utn.frba.dds.model.accionesentregas.AccionesSobreEntregas;
import ar.edu.utn.frba.dds.model.accionesentregas.NotificarAdmins;
import ar.edu.utn.frba.dds.model.accionesentregas.NotificarDonadorYDonante;
import ar.edu.utn.frba.dds.model.fallaentrega.EntregaVencida;
import ar.edu.utn.frba.dds.model.fallaentrega.ImprevistoLogistico;
import ar.edu.utn.frba.dds.model.fallaentrega.MotivoFallo;
import ar.edu.utn.frba.dds.model.fallaentrega.NoRecepcionada;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Entrega {
  private String id; // id propio de la entrega
  private EstadoEntrega estado;
  private String direccion;
  private int donacionId; // ids de la donacion/donaciones que se entregan
  private LocalDate fechaVencimiento; // si aplica a la donacion -> ej. si es perecedero
  private boolean visitado = false;
  private MotivoFallo motivoFallo;
  private String foto;
  private List<AccionesSobreEntregas> accionesSobreEntregas = new ArrayList<>();

  public Entrega(String direccion, int idDonacion) {
    this.estado = EstadoEntrega.PENDIENTE;
    this.donacionId = idDonacion;
    this.direccion = direccion;
    this.id = UUID.randomUUID().toString();
  }
  public LocalDate getFechaVencimiento() {
    return fechaVencimiento;
  }

  public void setFechaVencimiento(LocalDate fechaVencimiento) {
    this.fechaVencimiento = fechaVencimiento;
  }

  public int getDonacionId() {
    return this.donacionId;
  }

  public void setVisitado(boolean visitado) {
    this.visitado = visitado;
  }

  public String getDireccion() {
    return this.direccion;
  }

  public boolean getVisitado() {
    return this.visitado;
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
    visitado = true;
    estado = EstadoEntrega.ENTREGADA;
  }

  public void confirmarEntrega(String url_foto) {
    setFoto(foto);
  }

  public void marcarComoFallida(MotivoFallo motivo) {
    estado = EstadoEntrega.FALLIDA;
    setMotivoFallo(motivo);
    accionesSobreEntregas.forEach(accion ->
        accion.notificarFalloEntrega(this));
  }

  public void marcarRegresoADeposito() {
    estado = EstadoEntrega.PENDIENTE;
  }

  public void marcarComoNoRecepcionada() {
    marcarComoFallida(new NoRecepcionada());
  }

  public boolean estaVencida() {
    marcarComoFallida(new EntregaVencida());
    return fechaVencimiento != null && fechaVencimiento.isBefore(LocalDate.now());
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
}
