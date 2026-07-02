package ar.edu.utn.frba.dds.Estado;


import ar.edu.utn.frba.dds.Bienes.Donacion;
import ar.edu.utn.frba.dds.EstadoDonacion;

public class EntregaFallida implements EstadoDonacion {
  @Override
  public void asignar(Donacion donacion) {

  }

  @Override
  public void planificarRuta(Donacion donacion) {

  }

  @Override
  public void iniciarTraslado(Donacion donacion) {

  }

  @Override
  public void confirmarEntrega(Donacion donacion) {

  }

  @Override
  public void fallarEntrega(Donacion donacion, String justificacion) {
    donacion.setJustificacionFallo(justificacion);
    donacion.setEstado(new EnDeposito()); // Retorna al depósito
  }

  @Override
  public void vencer(Donacion donacion) {

  }

  @Override
  public String getNombre() { return "Entrega fallida"; }
}