package ar.edu.utn.frba.dds.Estado;

import ar.edu.utn.frba.dds.Bienes.DonacionSegmentada;

public class EnTraslado implements EstadoDonacion {

  @Override
  public void confirmarEntrega(DonacionSegmentada donacion) {
    donacion.setEstado(new Entregada());
  }

  @Override
  public void fallarEntrega(DonacionSegmentada donacion, String justificacion) {
    donacion.setJustificacionFallo(justificacion);
    donacion.setEstado(new EnDeposito());
  }

  @Override
  public void asignar(DonacionSegmentada donacion) {
    throw new IllegalStateException("No se puede asignar, la donación ya está en viaje.");
  }

  @Override
  public void planificarRuta(DonacionSegmentada donacion) {
    throw new IllegalStateException("La ruta ya fue planificada y se está ejecutando.");
  }

  @Override
  public void iniciarTraslado(DonacionSegmentada donacion) {
    throw new IllegalStateException("El traslado ya se encuentra en curso.");
  }

  @Override
  public void vencer(DonacionSegmentada donacion) {
    throw new IllegalStateException("No se puede vencer una donación que está en tránsito.");
  }

  @Override
  public String getNombre() {
    return "En traslado";
  }
}
