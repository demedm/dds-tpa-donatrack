package ar.edu.utn.frba.dds.repositories;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.model.necesidad.Necesidad;

public class NecesidadRepository {
  private List<Necesidad> necesidades = new ArrayList<>();
  public static NecesidadRepository Instance = new NecesidadRepository();

  private Integer contadorId =1;

  public Necesidad crear(Necesidad necesidad) {
    necesidad.setId("nec-" + contadorId++);
    necesidades.add(necesidad);
    return necesidad;
  }

  // public void evaluarPeticionesRecurrentes(GestorDonaciones gestor) {
  //   //Ordena a las que son recurrentes por cuantos dias le queden apra vencer
  //   necesidades.stream()
  //       .filter(n -> n instanceof NecesidadRecurrente)
  //       .map(n -> (NecesidadRecurrente) n)
  //       .sorted((a, b) -> a.getDiasAvencer() - b.getDiasAvencer())
  //       .forEach(n -> n.cumplirNecesidades(gestor));
  // }

  // public void evaluarPeticionesExtraordinarias(GestorDonaciones gestor) {
  //   //Va completandoa las ue son no recurrentes en abse a como estan en la lista
  //   necesidades.stream()
  //       .filter(n -> !(n instanceof NecesidadRecurrente))
  //       .forEach(n -> n.cumplirNecesidades(gestor));
  // }

  // public void evaluarTodasLasPeticiones(GestorDonaciones gestor) {
  //   // Completa segun el orden
  //   necesidades.forEach(n -> n.cumplirNecesidades(gestor));
  // }

  public Necesidad findById(String id){
    return this.necesidades.stream()
      .filter(n->n.getId().equals(id))
      .findFirst()
      .orElse(null);
  }

  //Obtener todas las necesidades 

  public List<Necesidad> findAll(){
    return necesidades;
  }

  public List<Necesidad> findAllRecurrentes(){
    return this.necesidades.stream()
      .filter(n-> n.getTipo() == Necesidad.TipoNecesidad.RECURRENTE)
      .toList();
  }

  public void eliminar(String id){
    this.necesidades.removeIf(n->n.getId().equals(id));
  }

  // actualizo el que esta en el repositorio

  public Necesidad actualizar(Necesidad necesidad){
    Necesidad existente = findById(necesidad.getId());

    existente.setPeticiones(necesidad.getPeticiones());

    return existente;
  }
}
