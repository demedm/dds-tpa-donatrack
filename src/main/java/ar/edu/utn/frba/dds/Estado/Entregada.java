package ar.edu.utn.frba.dds.Estado;

import ar.edu.utn.frba.dds.Bienes.DonacionSegmentada;

public class Entregada implements EstadoDonacion {

  @Override
  public void asignar(DonacionSegmentada donacion) {
    throw new IllegalStateException("La donación ya fue entregada con éxito.");
  }

  @Override
  public void planificarRuta(DonacionSegmentada donacion) {
    throw new IllegalStateException("La donación ya fue entregada, no se puede planificar otra ruta.");
  }

  @Override
  public void iniciarTraslado(DonacionSegmentada donacion) {
    throw new IllegalStateException("El traslado ya finalizó, la donación está en manos de la entidad.");
  }

  @Override
  public void confirmarEntrega(DonacionSegmentada donacion) {
    throw new IllegalStateException("La entrega ya fue confirmada previamente.");
  }

  @Override
  public void fallarEntrega(DonacionSegmentada donacion, String justificacion) {
    throw new IllegalStateException("No se puede fallar una entrega que ya fue realizada con éxito.");
  }

  @Override
  public void vencer(DonacionSegmentada donacion) {
    throw new IllegalStateException("No se puede vencer una donación que ya fue entregada.");
  }

  @Override
  public String getNombre() {
    return "Entregada";
  }
}