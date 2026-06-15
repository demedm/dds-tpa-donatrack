package ar.edu.utn.frba.dds;

import java.util.List;

public class Entidad {
  public String razonSocial;
  public List<MedioContacto> mediosContacto;
  public MedioContacto tipoContactoParaNotificaciones;
  public List<Mail> mailRepresentantes;
  public List<Whatsapp> whatsappOpcionales;

  // los strings son temporales hasta que definamos una forma más prolija de definir

  public Entidad(String razon, List<Mail> mailList) {
    razonSocial = razon;
    mailRepresentantes = mailList;
  }

  public void setMedioContacto(List<MedioContacto> medioContactoList) {
    mediosContacto = medioContactoList;
  }

  public void setMedioPreferido(MedioContacto medio) { this.tipoContactoParaNotificaciones = medio; }

  public MedioContacto getTipoContactoParaNotificaciones() {
    return this.tipoContactoParaNotificaciones;
    }

}
