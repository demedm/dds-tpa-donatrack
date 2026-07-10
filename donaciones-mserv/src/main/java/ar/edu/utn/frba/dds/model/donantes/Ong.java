package ar.edu.utn.frba.dds.model.donantes;

import ar.edu.utn.frba.dds.model.medioscontacto.Mail;

public class Ong extends PersonaJuridica {
  private String pathHistorialDonantes;

  public Ong() {}

  public void setPathHistorialDonantes(String pathHistorialDonantes) {
    this.pathHistorialDonantes = pathHistorialDonantes;
  }

}
