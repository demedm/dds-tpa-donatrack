package ar.edu.utn.frba.dds.model.usuarios;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@DiscriminatorValue("CH")
public class Chofer extends Usuario {
  private String nombre;
  private String apellido;

  public Chofer() {}

  public Chofer(String email, String nombre, String apellido) {
    super(email);
    this.nombre = nombre;
    this.apellido = apellido;
  }

  public String getNombre() {
    return nombre;
  }

  public String getApellido() {
    return apellido;
  }
}
