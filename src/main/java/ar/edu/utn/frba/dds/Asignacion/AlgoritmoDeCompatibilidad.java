package ar.edu.utn.frba.dds.Asignacion;

import ar.edu.utn.frba.dds.Bienes.DonacionSegmentada;
import ar.edu.utn.frba.dds.EntidadBeneficiaria;

import java.util.Comparator;
import java.util.List;

public class AlgoritmoDeCompatibilidad implements AlgoritmoAsignacion {

  int maxEntidades = 10;

  @Override
  public List<EntidadBeneficiaria> obtenerRanking(DonacionSegmentada donacion, List<EntidadBeneficiaria> entidades) {

    return entidades.stream()
        .sorted(Comparator.comparingLong(entidad -> this.calcularCompatibilidad(entidad, donacion)))
        .limit(maxEntidades)
        .toList();
  }

  private long calcularCompatibilidad(EntidadBeneficiaria entidad, DonacionSegmentada donacion) {

    return entidad.getNecesidades().stream()
        .flatMap(n -> n.getPeticiones().stream())
        .filter(p -> p.getSubclase().equals(donacion.getSucategoria()))
        .count();
  }
}
