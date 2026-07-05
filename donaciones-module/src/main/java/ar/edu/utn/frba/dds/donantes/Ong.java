package ar.edu.utn.frba.dds.donantes;

import ar.edu.utn.frba.dds.medioscontacto.Mail;

public class Ong extends PersonaJuridica {
  private String pathHistorialDonantes;

  public Ong(String razon, Mail mail, TipoEntidadJuridica tipo, Identificacion cuitEntidad, String rubro) {
    super(razon, mail, tipo, cuitEntidad, rubro);
  }
}
