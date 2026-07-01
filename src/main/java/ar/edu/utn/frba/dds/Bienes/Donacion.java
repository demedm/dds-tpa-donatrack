package ar.edu.utn.frba.dds.Bienes;

import ar.edu.utn.frba.dds.donantes.Persona;

import java.util.List;


public class Donacion {
  private String descripcionGeneral;
  private List<DonacionSegmentada> donacionesSegmentadas;
  private Persona donante;

  public Donacion(String descripcionGeneral, List<Bien> bienes, Persona donante) {
    this.descripcionGeneral = descripcionGeneral;
    this.donacionesSegmentadas = this.segmentar(bienes);
    this.donante = donante;
  }

  private List<DonacionSegmentada> segmentar(List<Bien> bienes) {

    List<Criterio> bienesUnicos = bienes.stream()
        .map(Bien::getCriterioDeAgrupacion)
        .distinct()
        .toList();

    return bienesUnicos.stream()
        .map(bienCriterio ->{
          List<Bien> grupo = bienes.stream()
              .filter(elemento -> elemento.getCriterioDeAgrupacion().equals(bienCriterio))
              .toList();

          return new DonacionSegmentada(
              grupo.size(),
              bienCriterio.subcategoria(),
              grupo.getFirst()
          );
        })
        .toList();
  }


/*



*/



}
