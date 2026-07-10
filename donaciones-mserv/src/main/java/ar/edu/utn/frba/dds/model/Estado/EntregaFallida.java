package ar.edu.utn.frba.dds.model.Estado;

import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;

public class EntregaFallida implements EstadoDonacion {
  @Override
  public void asignar(DonacionSegmentada donacion) {

  }

  @Override
  public void planificarRuta(DonacionSegmentada donacion) {

  }

  @Override
  public void iniciarTraslado(DonacionSegmentada donacion) {

  }

  @Override
  public void confirmarEntrega(DonacionSegmentada donacion) {

  }

  @Override
  public void fallarEntrega(DonacionSegmentada donacion, String justificacion) {
    donacion.setJustificacionFallo(justificacion);
    donacion.setEstado(new EnDeposito()); // Retorna al depósito
  }

  @Override
  public void vencer(DonacionSegmentada donacion) {

  }

  @Override
  public String getNombre() { return "Entrega fallida"; }
}