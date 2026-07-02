package ar.edu.utn.frba.dds.Estado;

import ar.edu.utn.frba.dds.Bienes.Donacion;
import ar.edu.utn.frba.dds.Entregada;
import ar.edu.utn.frba.dds.EstadoDonacion;

public class EnTraslado implements EstadoDonacion {

  @Override
  public void confirmarEntrega(Donacion donacion) {
    donacion.setEstado(new Entregada());
  }

  @Override
  public void fallarEntrega(Donacion donacion, String justificacion) {
    donacion.setJustificacionFallo(justificacion);
    donacion.setEstado(new EnDeposito());
  }

  @Override
  public void asignar(Donacion donacion) {
    throw new IllegalStateException("No se puede asignar, la donación ya está en viaje.");
  }

  @Override
  public void planificarRuta(Donacion donacion) {
    throw new IllegalStateException("La ruta ya fue planificada y se está ejecutando.");
  }

  @Override
  public void iniciarTraslado(Donacion donacion) {
    throw new IllegalStateException("El traslado ya se encuentra en curso.");
  }

  @Override
  public void vencer(Donacion donacion) {
    throw new IllegalStateException("No se puede vencer una donación que está en tránsito.");
  }

  @Override
  public String getNombre() {
    return "En traslado";
  }
}
