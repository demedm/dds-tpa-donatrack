package ar.edu.utn.frba.dds.model;

import ar.edu.utn.frba.dds.model.fallaentrega.ImprevistoLogistico;
import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Ruta {
  @Id
  @GeneratedValue
  private Long id1;

  @OneToMany
  @JoinColumn(name = "entrega_id")
  private List<Entrega> entregas;

  private String patenteAsignada;
  private String id;

  public Ruta(String patenteCamion, List<Entrega> entregas) {
    this.entregas = entregas;
    this.patenteAsignada = patenteCamion;
    this.id = UUID.randomUUID().toString();
  }

  public Ruta() {}

  public String getId() {
    return this.id;
  }

  public void agregarEntrega(Entrega entrega) {
    entregas.add(entrega);
  }

  public String getPatenteAsignada() {
    return this.patenteAsignada;
  }

  public Long getId1() {
    return id1;
  }

  public void setId1(Long id1) {
    this.id1 = id1;
  }

  public List<Entrega> getEntregas() {
    return this.entregas;
  }

  public void iniciarRuta() {
    entregas.forEach(Entrega::marcarComoIniciada);
  }

  public void visitarParada(String direccion) {
    entregas.stream().filter(entrega ->
        entrega.getDireccion().equals(direccion))
        .forEach(Entrega::marcarComoEntregada);
  }

  public void indicarImprovistoLogistico() {
    entregas.forEach(entrega -> entrega
        .marcarComoFallida(new ImprevistoLogistico()));
  }

  public void finalizarRuta() {
    entregas.stream().filter(entrega ->
            !entrega.getEntregado() && entrega.getMotivoFallo() != null)
        .forEach(Entrega::marcarRegreso);
  }

  public double calcularPorcentajeAvance() {
    if (entregas == null || entregas.isEmpty()) {
      return 0.0;
    }
    long entregadas = entregas.stream().filter(Entrega::getEntregado).count();
    return (double) entregadas / entregas.size() * 100.0;
  }
}