package ar.edu.utn.frba.dds.exceptions;

public class UsuarioNotFoundException extends RuntimeException {
  public UsuarioNotFoundException(Long id) {
    super(String.format("No se encontró el Usuario con el Id '%d'", id));
  }
}
