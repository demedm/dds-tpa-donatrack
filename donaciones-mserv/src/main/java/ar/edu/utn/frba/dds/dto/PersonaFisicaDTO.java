package ar.edu.utn.frba.dds.dto;

import ar.edu.utn.frba.dds.model.donantes.Genero;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.donantes.PersonaFisica;

public class PersonaFisicaDTO extends PersonaDTO {
  private String apellido;
  private Integer edad;
  private String genero;
  private String direccionActual;

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public Integer getEdad() {
    return edad;
  }

  public void setEdad(Integer edad) {
    this.edad = edad;
  }

  public String getGenero() {
    return genero;
  }

  public void setGenero(String genero) {
    this.genero = genero;
  }

  public String getDireccionActual() {
    return direccionActual;
  }

  public void setDireccionActual(String direccionActual) {
    this.direccionActual = direccionActual;
  }

  @Override
  public Persona PersonaDto() {
    PersonaFisica personaFisica = new PersonaFisica();
    completarDatosComunes(personaFisica);
    personaFisica.setApellido(apellido);
    personaFisica.setEdad(edad);
    personaFisica.setGenero(Genero.valueOf(genero));
    personaFisica.setDireccionActual(direccionActual);
    return personaFisica;
  }

  @Override
  public void aplicarCambios(Persona persona) {
    aplicarCambiosComunes(persona);
    PersonaFisica personaFisica = (PersonaFisica) persona;
    if (apellido != null) {
      personaFisica.setApellido(apellido);
    }
    if (edad != null) {
      personaFisica.setEdad(edad);
    }
    if (genero != null) {
      personaFisica.setGenero(Genero.valueOf(genero));
    }
    if (direccionActual != null) {
      personaFisica.setDireccionActual(direccionActual);
    }
  }
}
