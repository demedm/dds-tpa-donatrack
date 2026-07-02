package ar.edu.utn.frba.dds.necesidad;

import java.util.ArrayList;
import java.util.List;

public class GestorNecesidades {
  public List<Necesidad> necesidades = new ArrayList<>();

  public void agregarNecesidad(Necesidad necesidad) {
    necesidades.add(necesidad);
  }

  public void evaluarPeticionesRecurrentes(GestorDonaciones gestor) {
    //Ordena a las que son recurrentes por cuantos dias le queden apra vencer
    necesidades.stream()
        .filter(n -> n instanceof NecesidadRecurrente)
        .map(n -> (NecesidadRecurrente) n)
        .sorted((a, b) -> a.getDiasAvencer() - b.getDiasAvencer())
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
