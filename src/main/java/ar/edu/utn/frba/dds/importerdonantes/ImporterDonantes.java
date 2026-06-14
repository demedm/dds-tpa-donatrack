package ar.edu.utn.frba.dds.importerdonantes;

import ar.edu.utn.frba.dds.donantes.Identificacion;
import ar.edu.utn.frba.dds.donantes.Persona;
import ar.edu.utn.frba.dds.medioscontacto.MedioContacto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImporterDonantes {

  private List<Persona> registroDonantes = new ArrayList<>(); // registro de donantes importados
  private ImporterParser parser;
  private ImporterScanner scanner;

  public ImporterParser getParser() {
    return parser;
  }

  public ImporterScanner getScanner() {
    return scanner;
  }

  public ImporterDonantes(List<Persona> registro) {
    this.registroDonantes = registro;
    this.scanner = new ImporterScanner();
    this.parser = new ImporterParser();
  }

  // info basica de contacto: documento, nombre, email, telefono
  public void actualizarDonantes(List<Persona> registroImportados) {
    boolean actualizado = false;
    registroImportados.forEach(persona -> {
      registroDonantes.stream().filter(p ->
          esMismaPersona(p, persona))
          .forEach(pActualizar -> actualizarPersona(pActualizar, persona));
      });
  }

  private boolean esMismaPersona(Persona persona, Persona persona2) {
    return persona.getNroIdentificacion() == persona2.getNroIdentificacion();
  }

  private void actualizarPersona(Persona persona, Persona nuevosDatos) {
    // No se modifica el documento ni el nombre de la persona
  }

  public void importarDonantes(String filePath) {
    List<String[]> nuevosDatos = scanner.scanCSVFile(filePath);
    List<Persona> nuevosDonantes = parser.parseCSVcontent(nuevosDatos);
    actualizarDonantes(nuevosDonantes);
  }
}
