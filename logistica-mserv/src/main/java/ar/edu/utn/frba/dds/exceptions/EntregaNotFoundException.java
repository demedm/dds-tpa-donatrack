package ar.edu.utn.frba.dds.exceptions;

public class EntregaNotFoundException extends RuntimeException {
  public EntregaNotFoundException(Long id) {
    super(String.format("No se encontró la Entrega con el Id '%d'", id));
  }
}
