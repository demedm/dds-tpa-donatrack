package ar.edu.utn.frba.dds.model.Estado;

import ar.edu.utn.frba.dds.model.Bienes.DonacionSegmentada;

public interface EstadoDonacion {
  void asignar(DonacionSegmentada unaDonacion);
  void planificarRuta(DonacionSegmentada unaDonacion);
  void iniciarTraslado(DonacionSegmentada unaDonacion);
  void confirmarEntrega(DonacionSegmentada unaDonacion);
  void fallarEntrega(DonacionSegmentada unaDonacion, String justificacion);
  void vencer(DonacionSegmentada unaDonacion);
  String getNombre();
}