package ar.edu.utn.frba.dds.necesidad;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.EntidadBeneficiaria;

public class GestorNecesidades {
  public List<Necesidad> necesidades = new ArrayList<>();

  public Necesidad crearNecesidad(EntidadBeneficiaria entidad) {
    Necesidad necesidad = new Necesidad(entidad);
    this.agregarNecesidad(necesidad);
    return necesidad;
  }

  public void agregarNecesidad(Necesidad necesidad) {
    necesidades.add(necesidad);
  }


  public void evaluarPeticionesRecurrentes(GestorDonaciones gestor) {
    //Ordena a las que son recurrentes por cuantos dias le queden apra vencer
    necesidades.stream()
        .filter(n -> n instanceof NecesidadRecurrente)
        .map(n -> (NecesidadRecurrente) n) //Especifico que lo que hay son necesidades recurrentes
        .sorted((a, b) -> Long.compare(a.getDiasAvencer(), b.getDiasAvencer()))
        .forEach(n -> n.cumplirNecesidades(gestor));
  }

  public void evaluarPeticionesExtraordinarias(GestorDonaciones gestor) {
    //Va completandoa las ue son no recurrentes en abse a como estan en la lista
    necesidades.stream()
        .filter(n -> !(n instanceof NecesidadRecurrente))
        .forEach(n -> n.cumplirNecesidades(gestor));
  }

  public void evaluarTodasLasPeticiones(GestorDonaciones gestor) {
    // Completa segun el orden
    necesidades.forEach(n -> n.cumplirNecesidades(gestor));
  }
}