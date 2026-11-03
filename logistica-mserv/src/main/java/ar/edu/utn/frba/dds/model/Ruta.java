package ar.edu.utn.frba.dds.model;

import ar.edu.utn.frba.dds.model.fallaentrega.MotivoFallo;
import ar.edu.utn.frba.dds.model.usuarios.Chofer;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Ruta {
  @Id
  @GeneratedValue
  private Long id;

  @OneToMany
  @JoinColumn(name = "ruta_id")
  private List<Entrega> entregas = new ArrayList<>();

  @ManyToOne
  private Camion camion;

  @ManyToOne
  private Chofer chofer;

  @Enumerated(EnumType.STRING)
  private EstadoRuta estado;

  public Ruta(List<Entrega> entregas) {
    this.entregas = entregas;
    this.estado = EstadoRuta.NO_INICIADA;
  }

  public Ruta() {
    this.estado = EstadoRuta.NO_INICIADA;
  }

  public Long getId() {
    return this.id;
  }

  public void agregarEntrega(Entrega entrega) {
    entregas.add(entrega);
  }

  public void setEntregas(List<Entrega> entregas) {
    if (this.entregas != null) {
      this.entregas.clear();
    } else {
      this.entregas = new ArrayList<>();
    }
    if (entregas != null) {
      this.entregas.addAll(entregas);
    }
  }

  public List<Entrega> getEntregas() {
    return this.entregas;
  }

  public void iniciarRuta() {
    estado = EstadoRuta.EN_CURSO;
    if (camion != null) {
      camion.iniciarRuta();
    }
    entregas.forEach(Entrega::marcarComoIniciada);
  }

  public void indicarImprevistoLogistico() {
    estado = EstadoRuta.CANCELADA;
    entregas.stream()
        .filter(e -> e.getEstado() != EstadoEntrega.ENTREGADA)
        .forEach(e -> e.marcarComoFallida(MotivoFallo.IMPREVISTO_LOGISTICO));
  }

  public void finalizarRuta() {
    estado = EstadoRuta.FINALIZADA;
    entregas.stream().filter(entrega ->
            !entrega.getEstado().equals(EstadoEntrega.ENTREGADA) &&
                entrega.getMotivoFallo() != null)
        .forEach(Entrega::marcarRegreso);
  }

  public double calcularPorcentajeAvance() {
    if (entregas == null || entregas.isEmpty()) {
      return 0.0;
    }
    long entregadas = entregas.stream().filter(entrega ->
        entrega.getEstado().equals(EstadoEntrega.ENTREGADA)).count();
    return (double) entregadas / entregas.size() * 100.0;
  }

  public Chofer getChofer() {
    return chofer;
  }

  public void setChofer(Chofer chofer) {
    this.chofer = chofer;
  }

  public EstadoRuta getEstado() {
    return estado;
  }

  public void setEstado(EstadoRuta estado) {
    this.estado = estado;
  }

  public void asignarCamion(Camion camion) {
    this.camion = camion;
    if (camion != null) {
      camion.asignarRuta();
    }
  }

  public void setCamion(Camion camion) {
    asignarCamion(camion);
  }

  public Camion getCamion() {
    return camion;
  }

  public void asignarChofer(Chofer chofer) {
    this.chofer = chofer;
  }
}