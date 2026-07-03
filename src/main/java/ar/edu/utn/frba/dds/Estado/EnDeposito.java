package ar.edu.utn.frba.dds.Estado;

import ar.edu.utn.frba.dds.Bienes.Donacion;
import ar.edu.utn.frba.dds.EstadoDonacion;

public class EnDeposito implements EstadoDonacion {

  @Override
  public void asignar(Donacion unaDonacion) {

    unaDonacion.setEstado(new AsignacionRealizada());
  }

  @Override
  public void planificarRuta(Donacion unaDonacion) {
  throw new IllegalStateException("No se puede planificar la ruta de una donacion que no fue asignada a una institucion");
  }

  @Override
  public void iniciarTraslado(Donacion unaDonacion) {
    throw new IllegalStateException("No se puede iniciar el traslado sin haber planificado la ruta primero.");
  }

  @Override
  public void confirmarEntrega(Donacion unaDonacion) {
    throw new IllegalStateException("No se puede confirmar la entrega si la donación ni siquiera salió del depósito.");
  }

  @Override
  public void fallarEntrega(Donacion unaDonacion, String justificacion) {
    throw new IllegalStateException("No se puede fallar la entrega de una donación que no está en traslado.");
  }

  @Override
  public void vencer(Donacion unaDonacion) {
    unaDonacion.setEstado(new Vencida());
  }
  @Override
  public String getNombre() { return "En depósito"; }
}
