package ar.edu.utn.frba.dds.model.Donaciones;

import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.Criterio;
import ar.edu.utn.frba.dds.model.donantes.Persona;


import java.util.List;
import java.util.stream.Collectors;

public class Donacion {
  private String descripcionGeneral;
  private List<DonacionSegmentada> donacionesSegmentadas;
  private Persona donante;
  private static Integer contador = 0;
  private Integer id = 0 ;

  public Donacion(String descripcionGeneral, List<Bien> bienes, Persona donante) {
    /*
    if(bienes == null || bienes.isEmpty()){
      throw new IllegalArgumentException("Una donación no puede crearse sin bienes");
    }*/

    this.descripcionGeneral = descripcionGeneral;
    this.donacionesSegmentadas = this.segmentar(bienes);
    this.donante = donante;
    this.id = contador++;
  }

  public void agregarDonaciones(DonacionSegmentada donacionSegmentada){
    donacionesSegmentadas.add(donacionSegmentada);
  }

  public Integer getId() {
    return id;
  }

  public Persona getDonante(){
    return donante;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public List<DonacionSegmentada> getDonaciones(){
    return donacionesSegmentadas;
  }

  public void setDonacionesSegmentadas(List <DonacionSegmentada> donacionesSegmentadasAct){
    donacionesSegmentadas = donacionesSegmentadasAct;
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
              grupo.stream().findFirst().orElse(null)
          );
        })
        .collect(Collectors.toList()); //Para crear una lista mutable
  }


/*



*/



}
