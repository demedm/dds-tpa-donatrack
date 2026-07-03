package ar.edu.utn.frba.dds.api.dtos;

import ar.edu.utn.frba.dds.donantes.Genero;
import ar.edu.utn.frba.dds.donantes.Identificacion;
import ar.edu.utn.frba.dds.donantes.PersonaFisica;
import ar.edu.utn.frba.dds.donantes.TipoDocumento;
import ar.edu.utn.frba.dds.medioscontacto.Mail;

public class PersonaFisicaDTO {
  public String nombre;
  public String mail;
  public String nroDocumento;
  public TipoDocumento tipoDocumento;
  public int edad;
  public Genero genero;
  public String direccion;

  public PersonaFisica toDomain() {
    return new PersonaFisica(
        new Mail(mail),
        nombre,
        new Identificacion(tipoDocumento, nroDocumento),
        null,
        edad,
        genero,
        direccion
    );
  }
}
