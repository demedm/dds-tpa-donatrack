package ar.edu.utn.frba.dds.exceptions;

public class CamionNotFoundException extends RuntimeException {
  public CamionNotFoundException(Long id) {
    super(String.format("No se encontró el Camion con el Id '%d'", id));
  }

  public CamionNotFoundException(String patente) {
    super(String.format("No se encontró el Camion con la patente '%s'", patente));
  }
}
