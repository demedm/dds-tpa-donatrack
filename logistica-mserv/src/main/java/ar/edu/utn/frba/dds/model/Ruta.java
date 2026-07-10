package ar.edu.utn.frba.dds.model;

import ar.edu.utn.frba.dds.model.accionesentregas.AccionesSobreEntregas;
import ar.edu.utn.frba.dds.model.fallaentrega.ImprevistoLogistico;

import java.util.List;
import java.util.UUID;

public class Ruta {
  private List<Entrega> entregas;
  private String patenteAsignada;
  private String id;

  public Ruta(String patenteCamion, List<Entrega> entregas) {
    this.entregas = entregas;
    this.patenteAsignada = patenteCamion;
    this.id = UUID.randomUUID().toString();
  }

  public String getId() {
    return this.id;
  }

  public void agregarEntrega(Entrega entrega) {
    entregas.add(entrega);
  }

  public String getPatenteAsignada() {
    return this.patenteAsignada;
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
            !entrega.getVisitado() && entrega.getMotivoFallo() != null)
        .forEach(Entrega::marcarRegresoADeposito);
  }

/*
  public static class Ubicacion {
    private Double latitud;
    private Double longitud;
    private LocalDateTime timestamp;

    public Ubicacion(Double latitud, Double longitud, LocalDateTime timestamp) {
      this.latitud = latitud;
      this.longitud = longitud;
      this.timestamp = timestamp;
    }

    public Double getLatitud() { return latitud; }
    public Double getLongitud() { return longitud; }
    public LocalDateTime getTimestamp() { return timestamp; }

  }
   */
public double calcularPorcentajeAvance() {
  if (entregas == null || entregas.isEmpty()) return 0.0;
  long entregadas = entregas.stream().filter(Entrega::getVisitado).count();
  return (double) entregadas / entregas.size() * 100.0;
}
}