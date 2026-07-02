package ar.edu.utn.frba.dds.Bienes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Donacion {
  private String descripcionGeneral;
  private List<DonacionSegmentada> donacionesSegmentadas;
  private Donante donante;

  public Donacion(String descripcionGeneral, List<ar.edu.utn.frba.dds.Bienes.Bien> bienes, Donante donante) {
    this.descripcionGeneral = descripcionGeneral;
    this.donacionesSegmentadas = this.segmentar(bienes);
    this.donante = donante;
  }


  private List<DonacionSegmentada> segmentar(List<ar.edu.utn.frba.dds.Bienes.Bien> bienes) {
    Map<Criterio, List<ar.edu.utn.frba.dds.Bienes.Bien>> agrupados = bienes.stream()
            .collect(Collectors.groupingBy(ar.edu.utn.frba.dds.Bienes.Bien::getClaveAgrupacion));
    return agrupados.entrySet().stream()
            .map(entry -> new DonacionSegmentada(
                    entry.getValue().size(), //
                    entry.getKey().subcategoria(),
                    entry.getValue(),
                    entry.getValue().
            ))
            .collect(Collectors.toList());
  }

/*
  public Donacion() {
    this.estadoActual = new EnDeposito();
  }

  public void setEstado(EstadoDonacion nuevoEstado) {
    this.estadoActual = nuevoEstado;
    this.historialEstados.add(new RegistroCambioEstado(nuevoEstado.getNombre(), LocalDateTime.now()));
  }

  public void setJustificacionFallo(String justificacion) {
    this.justificacionFallo = justificacion;
  }
  public List<RegistroCambioEstado> getHistorialEstados() {
    return historialEstados;}

  public void asignar() { estadoActual.asignar(this); }
  public void planificarRuta() { estadoActual.planificarRuta(this); }
  public void iniciarTraslado() { estadoActual.iniciarTraslado(this); }
  public void confirmarEntrega() { estadoActual.confirmarEntrega(this); }
  public void fallarEntrega(String justificacion) { estadoActual.fallarEntrega(this, justificacion); }
  public void vencer() { estadoActual.vencer(this); }
  public String getNombreEstado() { return estadoActual.getNombre(); }
*/



}
