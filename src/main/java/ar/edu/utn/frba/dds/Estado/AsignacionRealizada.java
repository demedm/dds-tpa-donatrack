package ar.edu.utn.frba.dds.Estado;

import ar.edu.utn.frba.dds.Bienes.DonacionSegmentada;

public class AsignacionRealizada implements EstadoDonacion {

  @Override
  public void planificarRuta(DonacionSegmentada donacion) {
    donacion.setEstado(new ListaParaEntregar());
  }

  @Override
  public void asignar(DonacionSegmentada donacion) {
    throw new IllegalStateException("La donación ya fue asignada a una entidad beneficiaria.");
  }

  @Override
  public void iniciarTraslado(DonacionSegmentada donacion) {
    throw new IllegalStateException("No se puede iniciar el traslado sin haber planificado la ruta primero.");
  }

  @Override
  public void confirmarEntrega(DonacionSegmentada donacion) {
    throw new IllegalStateException("No se puede confirmar la entrega si la donación ni siquiera salió del depósito.");
  }

  @Override
  public void fallarEntrega(DonacionSegmentada donacion, String justificacion) {
    throw new IllegalStateException("No se puede fallar la entrega de una donación que no está en traslado.");
  }

  @Override
  public void vencer(DonacionSegmentada donacion) {
    throw new IllegalStateException("No se puede marcar como vencida desde este estado (debe estar en depósito).");
  }

  @Override
  public String getNombre() {
    return "Asignación realizada";
  }
}