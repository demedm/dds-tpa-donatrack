package ar.edu.utn.frba.dds.exceptions;

public class PersonaNotFoundException extends RuntimeException {
  public PersonaNotFoundException(String email) {
    super("No se encontró a una persona con el email " + email);
  }
}
