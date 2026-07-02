package ar.edu.utn.frba.dds.Logistica.AccionesRutas;

import ar.edu.utn.frba.dds.Logistica.AccionesSobreRutas;
import ar.edu.utn.frba.dds.Logistica.Ruta;

import java.util.List;

public class ReplanificarRuta implements AccionesSobreRutas {
  private List<Ruta> pendientesReasignar;

  @Override
  public void actualizarRuta(Ruta ruta, boolean asignada) {
    if(!asignada) {
      pendientesReasignar.add(ruta);
    }
  }
}
