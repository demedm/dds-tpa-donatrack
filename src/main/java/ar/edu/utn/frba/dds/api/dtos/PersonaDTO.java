package ar.edu.utn.frba.dds.api.dtos;

import ar.edu.utn.frba.dds.donantes.Persona;
import ar.edu.utn.frba.dds.medioscontacto.Telefono;

public class PersonaDTO {
  public String nombre;
  public String telefono;

  public Persona convertirDtoAObjeto() {

    Persona persona = new Persona(nombre, null, null, null);
    persona.setTelefono(new Telefono(telefono));

    return persona;
  }
}