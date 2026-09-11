package ar.edu.utn.frba.dds.model.usuarios;

import ar.edu.utn.frba.dds.model.Entrega;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ADM")
public class Administrador extends Usuario {
  public Administrador() {}

  public void confirmarReplanificacion(Entrega entrega) {

  }

}
