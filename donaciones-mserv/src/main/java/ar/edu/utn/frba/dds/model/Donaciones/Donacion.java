package ar.edu.utn.frba.dds.model.Donaciones;

import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.Criterio;
import ar.edu.utn.frba.dds.model.donantes.Persona;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.model.necesidad.Necesidad;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "donaciones")

public class Donacion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String descripcionGeneral;

  @Column(name = "fecha_recepcion")
  private LocalDate fechaRecepcion;

  @ManyToOne
  @JoinColumn(name = "donante_id")
  private Persona donante;
  
  @OneToMany(mappedBy = "donacion", 
      cascade = CascadeType.ALL, 
      orphanRemoval = true)
  
  private List<DonacionSegmentada> donacionesSegmentadas = new ArrayList<>();


  protected Donacion() {}

  public Donacion(String descripcionGeneral, List<Bien> bienes, Persona donante) {

    if (bienes == null || bienes.isEmpty()) {
      throw new IllegalArgumentException("Una donación no puede crearse sin bienes");
    }
    if (donante == null) {
      throw new IllegalArgumentException("Una donación necesita un donante");
    }

    this.descripcionGeneral = descripcionGeneral;
    this.donante = donante;
    this.donacionesSegmentadas = this.segmentar(bienes);
    this.fechaRecepcion = LocalDate.now();
  }

  public void setDonacionesSegmentadas(List <DonacionSegmentada> nuevas){
    this.donacionesSegmentadas.clear();
    this.donacionesSegmentadas.addAll(nuevas);
  }

  public Long getId() {
    return id;
  }

  @JsonIgnore
  public Persona getDonante(){
    return donante;
  }

  public List<DonacionSegmentada> getDonaciones(){
    return donacionesSegmentadas;
  }

  private List<DonacionSegmentada> segmentar(List<Bien> bienes) {

    List<Criterio> criteriosUnicos = bienes.stream()
        .map(Bien::getCriterioDeAgrupacion)
        .distinct()
        .toList();

    return criteriosUnicos.stream()
        .map(criterio -> {
          List<Bien> grupo = bienes.stream()
              .filter(bien -> bien.getCriterioDeAgrupacion().equals(criterio))
              .toList();

          return new DonacionSegmentada(this, grupo);
        })
        .collect(Collectors.toList());
  }

  public String getDescripcionGeneral() {
    return descripcionGeneral;
  }

  public void setDescripcionGeneral(String descripcionGeneral) {
    this.descripcionGeneral = descripcionGeneral;
  }

  public LocalDate getFechaRecepcion() {
    return fechaRecepcion;
  }

  public List<DonacionSegmentada> getSegmentadas() {
    return Collections.unmodifiableList(donacionesSegmentadas);
  }

  /*
  public List<DonacionSegmentada> asignarA(Necesidad necesidad){
    List<DonacionSegmentada> segmentadas = new ArrayList<>();

    for(DonacionSegmentada segmentada : donacionesSegmentadas){
      necesidad.getPeticiones().stream()
          .filter(segmentada::puedeCubrir)
          .findFirst()
          .ifPresent(peticion -> {
            segmentada.asignarA(necesidad.getEntidadId());
            peticion.agregarCantidadRecibida(segmentada.getCantidad(), String.valueOf(segmentada.getId()));
            segmentadas.add(segmentada);
          });
    }
    return segmentadas;
  public void agregarDonaciones(DonacionSegmentada donacionSegmentada){
    donacionesSegmentadas.add(donacionSegmentada);
  }

  }
  */

}
