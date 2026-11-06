package ar.edu.utn.frba.dds.exceptions;

public class CredencialesInvalidasException extends RuntimeException {
  public CredencialesInvalidasException(String email) {
    super("Credenciales invalidas para " + email);
  }
}
