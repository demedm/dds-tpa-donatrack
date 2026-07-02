package ar.edu.utn.frba.dds.donantes;

import ar.edu.utn.frba.dds.medioscontacto.Mail;
import ar.edu.utn.frba.dds.medioscontacto.MedioContacto;
import ar.edu.utn.frba.dds.medioscontacto.Telefono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PersonaJuridica extends Persona {
  private String rubro;
  private TipoEntidadJuridica tipo;
  private List<MedioContacto> mediosContacto = new ArrayList<>();

  public PersonaJuridica(String razon, Mail mail, TipoEntidadJuridica tipo,
                         Identificacion cuitEntidad, String rubro) {
    super(razon, mail, cuitEntidad, TipoPersona.JURIDICA);
    this.tipo = tipo;
    this.rubro = rubro;
  }

  public void setMedioContacto(MedioContacto contacto) {
    this.mediosContacto.add(contacto);
  }

  public void setMedioPreferido(MedioContacto medio) {
    super.setMedioPreferido(medio);
  }

  //Gubernamental, ONG, Empresa, Institución
  private TipoEntidadJuridica getTipoEntidad(String nombreEntidad) {
    if (nombreEntidad.contains("Fundación") || nombreEntidad.contains("Asociación Civil")) {
      return TipoEntidadJuridica.ONG;
    }
    if (nombreEntidad.contains("S.A.")
        || nombreEntidad.contains("S.R.L.")
        || nombreEntidad.contains("S.A.S.")) {
      return TipoEntidadJuridica.EMPRESA;
    }
    if (nombreEntidad.contains("Cooperativa")) {
      return TipoEntidadJuridica.INSTITUCION;
    }
    return TipoEntidadJuridica.GUBERNAMENTAL;
  }

}
