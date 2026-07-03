package ar.edu.utn.frba.dds.api.dtos;

import ar.edu.utn.frba.dds.donantes.Identificacion;
import ar.edu.utn.frba.dds.donantes.PersonaJuridica;
import ar.edu.utn.frba.dds.donantes.TipoDocumento;
import ar.edu.utn.frba.dds.donantes.TipoEntidadJuridica;
import ar.edu.utn.frba.dds.medioscontacto.Mail;

public class PersonaJuridicaDTO {
  public String razonSocial;
  public String mail;
  public String cuit;
  public TipoEntidadJuridica tipoEntidad;
  public String rubro;

  public PersonaJuridica convertirDtoAObjeto() {
    return new PersonaJuridica(razonSocial, new Mail(mail), tipoEntidad, new Identificacion(TipoDocumento.CUIT, cuit), rubro);
  }
}
