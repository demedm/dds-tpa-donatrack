package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.medioscontacto.Mail;
import java.util.List;

public class Ong extends Entidad {
  private List<Donante> historialDonantes;

  public Ong(String razon, List<Mail> representantesMailList) {
    razonSocial = razon;
    mailRepresentantes = representantesMailList;
  }

}
