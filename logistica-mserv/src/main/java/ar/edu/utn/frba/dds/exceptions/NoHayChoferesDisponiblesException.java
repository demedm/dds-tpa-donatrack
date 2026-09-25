package ar.edu.utn.frba.dds.exceptions;

public class NoHayChoferesDisponiblesException extends RuntimeException {
  public NoHayChoferesDisponiblesException() {
    super("No se pueden crear rutas nuevas si no hay choferes disponibles.");
  }
}
