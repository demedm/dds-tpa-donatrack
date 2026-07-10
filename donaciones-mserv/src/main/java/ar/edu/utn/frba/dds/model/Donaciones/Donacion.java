package ar.edu.utn.frba.dds.model.Donaciones;

import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.Criterio;
import ar.edu.utn.frba.dds.model.donantes.Persona;

import java.util.List;

public class Donacion {
  private String descripcionGeneral;
  private List<DonacionSegmentada> donacionesSegmentadas;
  private Persona donante;
  private String id;

  public Donacion(String descripcionGeneral, List<Bien> bienes, Persona donante) {

    if(bienes.isEmpty() || bienes == null){
      throw new IllegalArgumentException("Una donación no puede crearse sin bienes");
    }
    this.descripcionGeneral = descripcionGeneral;
    this.donacionesSegmentadas = this.segmentar(bienes);
    this.donante = donante;
  }

  public void agregarDonaciones(DonacionSegmentada donacionSegmentada){
    donacionesSegmentadas.add(donacionSegmentada);
  }

  public String getId() {
    return id;
  }

  public Persona getDonante(){
    return donante;
  }

  public void setId(String id) {
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
        .toList();
  }


/*



*/



}
