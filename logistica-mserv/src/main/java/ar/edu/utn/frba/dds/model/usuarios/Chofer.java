package ar.edu.utn.frba.dds.model.usuarios;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@DiscriminatorValue("CH")
public class Chofer extends Usuario {
  @Id
  @GeneratedValue
  private Long id;

  private String nombre;
  private String apellido;

  public Chofer() {}

  public Chofer(String nombre, String apellido) {
    this.nombre = nombre;
    this.apellido = apellido;
  }

}
