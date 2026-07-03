package ar.edu.utn.frba.dds.api.repository;

import ar.edu.utn.frba.dds.Bienes.Donacion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RegistroDonacion {
  private static List<Donacion> donaciones = new ArrayList<>();

  public static void registrar(Donacion donacion) {
    donaciones.add(donacion);
  }

  public static List<Donacion> getDonaciones() {
    return donaciones;
  }

  public static Optional<Donacion> buscarPorId(String id) {
    return donaciones.stream().filter(d -> d.getId().equals(id)).findFirst();
  }

}
