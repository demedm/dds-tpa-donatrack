package ar.edu.utn.frba.dds.model.accionesrutas;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.EstadoCamion;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.repositories.CamionRepositorio;

public class AsignarCamion implements AccionesSobreRutas{

  @Override
  public void actualizarRuta(Ruta ruta, boolean asignada) {
    if (asignada) {
      Camion camion = CamionRepositorio.Instance.findByPatente(ruta.getPatenteAsignada());
      if (camion != null) {
        camion.asignarRuta(ruta);
        camion.setEstado(EstadoCamion.RUTA_ASIGNADA);
      }
    }
  }
}
