package ar.edu.utn.frba.dds.model.Asignacion;

import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;

import java.util.Comparator;
import java.util.List;

public class AlgoritmoDeCompatibilidad implements AlgoritmoAsignacion {

  int maxEntidades = 10;

  @Override
  public String nombre() {
    return "COMPATIBILIDAD";
  }


  @Override
  public List<EntidadBeneficiaria> obtenerRanking(DonacionSegmentada donacion, List<EntidadBeneficiaria> entidades) {

    return entidades.stream()
        .filter(entidad -> this.calcularCompatibilidad(entidad,donacion) > 0)
        .sorted(Comparator.comparingLong(
                (EntidadBeneficiaria entidad) -> this.calcularCompatibilidad(entidad, donacion)).reversed())
        .limit(maxEntidades)
        .toList();
  }

  private long calcularCompatibilidad(EntidadBeneficiaria entidad, DonacionSegmentada donacion) {

    return entidad.getNecesidades().stream()
        .flatMap(n -> n.getPeticiones().stream())
        .filter(p -> p.getSubclase().equalsIgnoreCase(donacion.getSubcategoria().getDescripcion())
            && p.faltaAsignar())
        .count();
  }
}
