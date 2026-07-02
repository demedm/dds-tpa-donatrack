package ar.edu.utn.frba.dds.Estado;

import ar.edu.utn.frba.dds.Bienes.Donacion;

public class ListaParaEntregar implements EstadoDonacion {

  @Override
  public void iniciarTraslado(Donacion donacion) {
    // El camión asignado inicia el recorrido
    donacion.setEstado(new EnTraslado());
  }

  @Override
  public void asignar(Donacion donacion) {
    throw new IllegalStateException("La donación ya fue asignada y está en una ruta planificada.");
  }

  @Override
  public void planificarRuta(Donacion donacion) {
    throw new IllegalStateException("La donación ya se encuentra en una ruta planificada.");
  }

  @Override
  public void confirmarEntrega(Donacion donacion) {
    throw new IllegalStateException("No se puede confirmar la entrega porque el camión aún no inició el traslado.");
  }

  @Override
  public void fallarEntrega(Donacion donacion, String justificacion) {
    throw new IllegalStateException("No se puede fallar la entrega si el camión todavía no salió a hacer el reparto.");
  }

  @Override
  public void vencer(Donacion donacion) {
    throw new IllegalStateException("No se puede marcar como vencida desde este estado (debe estar en depósito).");
  }

  @Override
  public String getNombre() {
    return "Lista para entregar";
  }
}