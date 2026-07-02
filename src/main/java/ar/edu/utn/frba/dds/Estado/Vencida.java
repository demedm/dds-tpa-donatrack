package ar.edu.utn.frba.dds.Estado;

import ar.edu.utn.frba.dds.Bienes.Donacion;
import ar.edu.utn.frba.dds.EstadoDonacion;

public class Vencida implements EstadoDonacion {

  @Override
  public void asignar(Donacion donacion) {
    throw new IllegalStateException("No se puede asignar una donación vencida.");
  }

  @Override
  public void planificarRuta(Donacion donacion) {
    throw new IllegalStateException("No se puede planificar ruta para una donación vencida.");
  }

  @Override
  public void iniciarTraslado(Donacion donacion) {
    throw new IllegalStateException("No se puede trasladar una donación vencida.");
  }

  @Override
  public void confirmarEntrega(Donacion donacion) {
    throw new IllegalStateException("No se puede entregar una donación que está vencida.");
  }

  @Override
  public void fallarEntrega(Donacion donacion, String justificacion) {
    throw new IllegalStateException("Una donación vencida no puede registrar una entrega fallida.");
  }

  @Override
  public void vencer(Donacion donacion) {
    throw new IllegalStateException("La donación ya se encuentra vencida.");
  }

  @Override
  public String getNombre() {
    return "Vencida";
  }
}