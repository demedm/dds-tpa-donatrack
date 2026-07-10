package ar.edu.utn.frba.dds.model.importerdonantes;

import ar.edu.utn.frba.dds.model.donantes.Identificacion;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.donantes.PersonaFisica;
import ar.edu.utn.frba.dds.model.donantes.PersonaJuridica;
import ar.edu.utn.frba.dds.model.donantes.TipoDocumento;
import ar.edu.utn.frba.dds.model.donantes.TipoPersona;
import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.medioscontacto.Telefono;

import java.util.ArrayList;
import java.util.List;

public class ImporterParser {
  public List<Persona> parseCSVcontent(List<String[]> filas) {
    List<Persona> personas = new ArrayList<>();
    // TipoPersona,TipoDoc,Documento,Nombre/Razón Social,Email,Teléfono
    filas.forEach( fila -> {
          TipoPersona tipo = fila[0].contains("HUMANA") ?
              TipoPersona.FISICA : TipoPersona.JURIDICA;
          TipoDocumento documento = fila[1].contains("DNI") ?
              TipoDocumento.DNI : TipoDocumento.CUIT;
          Identificacion id = new Identificacion(documento, fila[2]);
          Mail mail = new Mail(fila[4]);
          Telefono telefono = new Telefono(fila[5]);
          Persona persona = tipo == TipoPersona.FISICA
              ? new PersonaFisica()
              : new PersonaJuridica();
          persona.setNombreIdentificador(fila[3]);
          persona.setMail(mail);
          persona.setIdentificacion(id);
          persona.setTelefono(telefono);
          personas.add(persona);
        }
      );
    return personas;
  }

}
