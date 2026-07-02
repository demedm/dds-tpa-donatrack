package ar.edu.utn.frba.dds.Bienes;

import java.util.List;

public class Donaciones {

  private List<Donacion> todaDonaciones;

  public List<Bien> segmentar(Caracteristica criterio, Subcategoria subcategoria) {

    return this.todaDonaciones.stream()
        .flatMap(donacion -> donacion.getContenido().stream())
        .filter(bien -> bien.getSubcategoria().equals(subcategoria))
        .filter(bien -> bien.getCaracteristica().coincideCon(criterio))
        .toList();
  }

}
