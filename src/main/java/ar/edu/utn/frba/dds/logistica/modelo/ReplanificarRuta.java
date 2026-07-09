package ar.edu.utn.frba.dds.logistica.modelo;

import java.util.List;

public class ReplanificarRuta implements AccionesSobreRutas {
  private List<Ruta> pendientesReasignar;

  @Override
  public void actualizarRuta(Ruta ruta, boolean asignada) {
    if (!asignada) {
      pendientesReasignar.add(ruta);
    }
  }
}
