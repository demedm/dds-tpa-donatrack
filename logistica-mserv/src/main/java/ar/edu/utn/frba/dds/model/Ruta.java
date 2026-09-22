package ar.edu.utn.frba.dds.model;

import ar.edu.utn.frba.dds.model.fallaentrega.ImprevistoLogistico;
import ar.edu.utn.frba.dds.model.usuarios.Chofer;
import java.time.LocalDateTime;
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
  private List<Entrega> entregas;

  @ManyToOne
  private Camion camion;

  @ManyToOne
  private Chofer chofer;

  @Enumerated(EnumType.STRING)
  private EstadoRuta estado;

  public Ruta(Chofer chofer, List<Entrega> entregas) {
    this.entregas = entregas;
    this.chofer = chofer;
    this.estado = EstadoRuta.NO_INICIADA;
  }

  public Ruta() {}

  public Long getId() {
    return this.id;
  }

  public void agregarEntrega(Entrega entrega) {
    entregas.add(entrega);
  }

  public void asignarCamion(Camion camion) {
    this.camion = camion;
  }

  public List<Entrega> getEntregas() {
    return this.entregas;
  }

  public void iniciarRuta() {
    estado = EstadoRuta.EN_CURSO;
    camion.iniciarRuta();
    entregas.forEach(Entrega::marcarComoIniciada);
  }

  public void visitarParada(String direccion, LocalDateTime fechaHoraEntrega) {
    entregas.stream().filter(entrega ->
        entrega.getDireccion().equals(direccion))
        .forEach(entrega -> entrega.marcarComoEntregada(camion, fechaHoraEntrega));
  }

  public void indicarImprovistoLogistico() {
    estado = EstadoRuta.CANCELADA;
    camion.improvistoLogistico();
    entregas.forEach(entrega -> entrega
        .marcarComoFallida(new ImprevistoLogistico()));
  }

  public void finalizarRuta() {
    estado = EstadoRuta.FINALIZADA;
    entregas.stream().filter(entrega ->
            !entrega.getParadaVisitada() && entrega.getMotivoFallo() != null)
        .forEach(Entrega::marcarRegreso);
  }

  public double calcularPorcentajeAvance() {
    if (entregas == null || entregas.isEmpty()) {
      return 0.0;
    }
    long entregadas = entregas.stream().filter(Entrega::getParadaVisitada).count();
    return (double) entregadas / entregas.size() * 100.0;
  }

  public Chofer getChofer() {
    return chofer;
  }

  public EstadoRuta getEstado() {
    return estado;
  }

  public void setCamion(Camion camion) {
    this.camion = camion;
    camion.asignarRuta();
  }

  public Camion getCamion() {
    return camion;
  }
}