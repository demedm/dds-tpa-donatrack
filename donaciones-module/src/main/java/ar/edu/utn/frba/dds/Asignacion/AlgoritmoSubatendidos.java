package ar.edu.utn.frba.dds.Asignacion;

import ar.edu.utn.frba.dds.Bienes.DonacionSegmentada;
import ar.edu.utn.frba.dds.entidad.EntidadBeneficiaria;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class AlgoritmoSubatendidos implements AlgoritmoAsignacion {

  int maximo_resultado=10;

  @Override
  public List<EntidadBeneficiaria> obtenerRanking(DonacionSegmentada donacion, List<EntidadBeneficiaria> entidades) {

    return entidades.stream()
        .sorted(Comparator.comparingLong(this::cantidadDonaciones))
        .limit(maximo_resultado)
        .toList();
  }

  public long cantidadDonaciones(EntidadBeneficiaria entidad) {
    LocalDate tresMesesAtras = LocalDate.now().minusMonths(3);
    return entidad.getDonacionesRecibidas().stream()
        .filter(don -> don.getFechaDeEntrega().isAfter(tresMesesAtras))
        .count();
  }

}
