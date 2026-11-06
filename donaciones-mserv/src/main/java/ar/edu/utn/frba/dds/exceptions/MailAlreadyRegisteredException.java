package ar.edu.utn.frba.dds.exceptions;

public class MailAlreadyRegisteredException extends RuntimeException {
  public MailAlreadyRegisteredException(String mail) {
    super("El mail " + mail + " ya se encuentra registrado.");
  }
}
