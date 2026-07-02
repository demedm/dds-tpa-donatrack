package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.donantes.Identificacion;
import ar.edu.utn.frba.dds.donantes.PersonaJuridica;
import ar.edu.utn.frba.dds.donantes.TipoEntidadJuridica;
import ar.edu.utn.frba.dds.medioscontacto.Mail;
import java.util.List;

public class Ong extends PersonaJuridica {
  private String pathHistorialDonantes;

  public Ong(String razon, Mail mail, TipoEntidadJuridica tipo, Identificacion cuitEntidad, String rubro) {
    super(razon, mail, tipo, cuitEntidad, rubro);
  }
}
