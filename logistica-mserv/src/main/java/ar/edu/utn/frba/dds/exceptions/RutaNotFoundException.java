package ar.edu.utn.frba.dds.exceptions;

public class RutaNotFoundException extends RuntimeException {
  public RutaNotFoundException(Long id) {
    super(String.format("No se encontró la Ruta con el Id '%d'", id));
  }
}
