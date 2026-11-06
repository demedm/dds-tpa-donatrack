package ar.edu.utn.frba.dds.exceptions;

public class UsuarioNotFoundException extends RuntimeException {

  public UsuarioNotFoundException(String email) {
    super(String.format("No se encontró usuario con email '%s'", email));
  }
}
