package ar.edu.utn.frba.dds.donantes;

import ar.edu.utn.frba.dds.medioscontacto.Mail;
import ar.edu.utn.frba.dds.medioscontacto.MedioContacto;
import ar.edu.utn.frba.dds.medioscontacto.Telefono;

import java.util.List;
import java.util.Objects;

public class PersonaJuridica extends Persona {
  private String rubro;
  private TipoEntidadJuridica tipo;
  private List<MedioContacto> mediosContacto;

  public PersonaJuridica(String razon, Mail mail, TipoEntidadJuridica tipo,
                         Identificacion cuitEntidad, String rubro) {
    super(razon, mail, cuitEntidad);
    this.tipo = tipo;
    this.rubro = rubro;
  }

  public void setMedioContacto(MedioContacto contacto) {
    mediosContacto.add(contacto);
  }

  public void setMedioPreferido(MedioContacto medio) {
    this.medioPreferido = medio;
  }

  @Override
  public MedioContacto getMedioPreferido() {
    return this.medioPreferido;
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
