package ar.edu.utn.frba.dds.exceptions;

public class DepositoNotFoundException extends RuntimeException {
  public DepositoNotFoundException(String message) {
    super(message);
  }

  public DepositoNotFoundException(Long id) {
    super(String.format("No se encontró un depósito con el Id '%d'", id));
  }
}
