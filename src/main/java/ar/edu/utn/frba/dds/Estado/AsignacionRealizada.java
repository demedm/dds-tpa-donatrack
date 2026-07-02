package ar.edu.utn.frba.dds.Estado;

import ar.edu.utn.frba.dds.Bienes.Donacion;
import ar.edu.utn.frba.dds.EstadoDonacion;
import ar.edu.utn.frba.dds.ListaParaEntregar;

public class AsignacionRealizada implements EstadoDonacion {

  @Override
  public void planificarRuta(Donacion donacion) {
    donacion.setEstado(new ListaParaEntregar());
  }

  @Override
  public void asignar(Donacion donacion) {
    throw new IllegalStateException("La donación ya fue asignada a una entidad beneficiaria.");
  }

  @Override
  public void iniciarTraslado(Donacion donacion) {
    throw new IllegalStateException("No se puede iniciar el traslado sin haber planificado la ruta primero.");
  }

  @Override
  public void confirmarEntrega(Donacion donacion) {
    throw new IllegalStateException("No se puede confirmar la entrega si la donación ni siquiera salió del depósito.");
  }

  @Override
  public void fallarEntrega(Donacion donacion, String justificacion) {
    throw new IllegalStateException("No se puede fallar la entrega de una donación que no está en traslado.");
  }

  @Override
  public void vencer(Donacion donacion) {
    throw new IllegalStateException("No se puede marcar como vencida desde este estado (debe estar en depósito).");
  }

  @Override
  public String getNombre() {
    return "Asignación realizada";
  }
}