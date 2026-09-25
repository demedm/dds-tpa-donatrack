package ar.edu.utn.frba.dds.model.Donaciones;

import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.Criterio;
import ar.edu.utn.frba.dds.model.donantes.Persona;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "donaciones")

public class Donacion {

  @Id
  private String id;

  private String descripcionGeneral;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "donacion_id")
  private List<DonacionSegmentada> donacionesSegmentadas = new ArrayList<>();

  @Transient
  /*
  luego pasarl oa
  @ManyToOne @JoinColumn(name ="donante_id")
  cuando la persona sea @Entity
   */
  @JsonIgnore
  private Persona donante;

  protected Donacion() {}

  public Donacion(String descripcionGeneral, List<Bien> bienes, Persona donante) {

    if(bienes == null || bienes.isEmpty()){
      throw new IllegalArgumentException("Una donación no puede crearse sin bienes");
    }
    this.descripcionGeneral = descripcionGeneral;
    this.donacionesSegmentadas = this.segmentar(bienes);
    this.donante = donante;
    this.id = UUID.randomUUID().toString();
  }

  public void agregarDonaciones(DonacionSegmentada donacionSegmentada){
    donacionesSegmentadas.add(donacionSegmentada);
  }

  public String getId() {
    return id;
  }
  @JsonIgnore
  public Persona getDonante(){
    return donante;
  }

  public List<DonacionSegmentada> getDonaciones(){
    return donacionesSegmentadas;
  }

  public void setDonacionesSegmentadas(List <DonacionSegmentada> nuevas){
    this.donacionesSegmentadas.clear();
    this.donacionesSegmentadas.addAll(nuevas);
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

  public String getDescripcionGeneral() {
    return descripcionGeneral;
  }

  public void setDescripcionGeneral(String descripcionGeneral) {
    this.descripcionGeneral = descripcionGeneral;
  }

  public void setId(String id){
    this.id=id;
  }

}
