package ar.edu.utn.frba.dds.model.Estado;

import ar.edu.utn.frba.dds.model.Bienes.DonacionSegmentada;

public class Vencida implements EstadoDonacion {

  @Override
  public void asignar(DonacionSegmentada donacion) {
    throw new IllegalStateException("No se puede asignar una donación vencida.");
  }

  @Override
  public void planificarRuta(DonacionSegmentada donacion) {
    throw new IllegalStateException("No se puede planificar ruta para una donación vencida.");
  }

  @Override
  public void iniciarTraslado(DonacionSegmentada donacion) {
    throw new IllegalStateException("No se puede trasladar una donación vencida.");
  }

  @Override
  public void confirmarEntrega(DonacionSegmentada donacion) {
    throw new IllegalStateException("No se puede entregar una donación que está vencida.");
  }

  @Override
  public void fallarEntrega(DonacionSegmentada donacion, String justificacion) {
    throw new IllegalStateException("Una donación vencida no puede registrar una entrega fallida.");
  }

  @Override
  public void vencer(DonacionSegmentada donacion) {
    throw new IllegalStateException("La donación ya se encuentra vencida.");
  }

  @Override
  public String getNombre() {
    return "Vencida";
  }
}