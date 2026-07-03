package ar.edu.utn.frba.dds.api.repository;

import ar.edu.utn.frba.dds.api.dtos.EntidadBeneficiariaDTO;
import ar.edu.utn.frba.dds.entidad.EntidadBeneficiaria;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntidadesRepository {

  private static List<EntidadBeneficiaria> entidades = new ArrayList<>();

  public static void registrar(EntidadBeneficiaria entidad) {
    entidades.add(entidad);
  }

  public static List<EntidadBeneficiaria> getEntidades() {
    return entidades;
  }

  public static Optional<EntidadBeneficiaria> buscarPorRazonSocial(String razonSocial) {
    return entidades.stream()
        .filter(e -> e.getRazonSocial().equals(razonSocial))
      .findFirst();
}

  public static void actualizarRazonSocial(String razonSocial, EntidadBeneficiariaDTO dto) {
    EntidadBeneficiaria existente = buscarPorRazonSocial(razonSocial)
        .orElseThrow(() -> new RuntimeException("No se encontró entidad con razon social: " + razonSocial));

    existente.actualizarDatos(dto);
  }

  public static void eliminarRazonSocial(String razonSocial) {
    EntidadBeneficiaria existente = buscarPorRazonSocial(razonSocial)
        .orElseThrow(() -> new RuntimeException("No se encontró entidad con razn social: " + razonSocial));
    entidades.remove(existente);
  }
}
