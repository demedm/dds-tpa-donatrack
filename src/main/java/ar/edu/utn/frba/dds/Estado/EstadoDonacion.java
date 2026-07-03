package ar.edu.utn.frba.dds.Estado;

import ar.edu.utn.frba.dds.Bienes.Donacion;

public interface EstadoDonacion {
  void asignar(Donacion unaDonacion);
  void planificarRuta(Donacion unaDonacion);
  void iniciarTraslado(Donacion unaDonacion);
  void confirmarEntrega(Donacion unaDonacion);
  void fallarEntrega(Donacion unaDonacion, String justificacion);
  void vencer(Donacion unaDonacion);
  String getNombre();
}