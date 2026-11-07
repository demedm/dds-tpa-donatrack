package ar.edu.utn.frba.dds.exceptions;

public class DonacionInexistenteException extends RuntimeException {

  public DonacionInexistenteException(String mensaje) {
    super(mensaje);
  }
}
