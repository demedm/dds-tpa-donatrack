package ar.edu.utn.frba.dds.model.usuarios;

import ar.edu.utn.frba.dds.model.Camion;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue("CH")
public class Chofer extends Usuario {
  @Id
  @GeneratedValue
  private Long id;

  @OneToOne
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
