package ar.edu.utn.frba.dds.model.accionesrutas;

import ar.edu.utn.frba.dds.model.Ruta;
import java.util.ArrayList;
import java.util.List;

public class ReplanificarRuta implements AccionesSobreRutas {
  private List<Ruta> pendientesReasignar = new ArrayList<>();

  @Override
  public void actualizarRuta(Ruta ruta, boolean asignada) {
    if (!asignada) {
      pendientesReasignar.add(ruta);
    }
  }
}