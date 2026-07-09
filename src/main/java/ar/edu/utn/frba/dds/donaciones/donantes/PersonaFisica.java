package ar.edu.utn.frba.dds.model.donantes;

import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;

public class PersonaFisica extends Persona {
  private String apellido;
  private int edad;
  private Genero genero;
  private String direccionActual;

  public void setGenero(Genero genero) {
    this.genero = genero;
  }

  public void setEdad(int edad) {
    this.edad = edad;
  }

  public void setDireccionActual(String direccion) {
    this.direccionActual = direccion;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public Genero getGenero() {
    return this.genero;
  }

  public int getEdad() {
    return this.edad;
  }

  public String getApellido() {
    return this.apellido;
  }

  public String getDireccionActual() {
    return direccionActual;
  }

}
