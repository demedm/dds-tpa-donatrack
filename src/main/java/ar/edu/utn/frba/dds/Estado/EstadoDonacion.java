package ar.edu.utn.frba.dds.Estado;

import ar.edu.utn.frba.dds.Bienes.DonacionSegmentada;

public interface EstadoDonacion {
  void asignar(DonacionSegmentada unaDonacion);
  void planificarRuta(DonacionSegmentada unaDonacion);
  void iniciarTraslado(DonacionSegmentada unaDonacion);
  void confirmarEntrega(DonacionSegmentada unaDonacion);
  void fallarEntrega(DonacionSegmentada unaDonacion, String justificacion);
  void vencer(DonacionSegmentada unaDonacion);
  String getNombre();
}