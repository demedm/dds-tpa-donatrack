package ar.edu.utn.frba.dds.model.usuarios;

import ar.edu.utn.frba.dds.model.Camion;

public class Chofer extends Usuario{
  private Camion camion;

  public Chofer() {}

  public void iniciarRuta() {
    camion.getRutaActual().iniciarRuta();
  }

  public void finalizarRuta() {
    camion.getRutaActual().finalizarRuta();
  }

  public void visitarParada(String direccionParada) {
    camion.getRutaActual().visitarParada(direccionParada);
  }

  public void improvistoLogistico() {
    camion.improvistoLogistico();
  }

  public Camion getCamion() {
    return camion;
  }

  public void setCamion(Camion camion) {
    this.camion = camion;
  }
}
