package ar.edu.utn.frba.dds.importerdonantes;

import ar.edu.utn.frba.dds.RegistroDonante;
import ar.edu.utn.frba.dds.donantes.Identificacion;
import ar.edu.utn.frba.dds.donantes.Persona;
import ar.edu.utn.frba.dds.medioscontacto.MedioContacto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImporterDonantes {

  private RegistroDonante registroDonantes; // registro de donantes importados
  private ImporterParser parser;
  private ImporterScanner scanner;

  public ImporterParser getParser() {
    return parser;
  }

  public ImporterScanner getScanner() {
    return scanner;
  }

  public ImporterDonantes(RegistroDonante registro) {
    this.registroDonantes = registro;
    this.scanner = new ImporterScanner();
    this.parser = new ImporterParser();
  }

  public void importarDonantes(String filePath) {
    List<String[]> nuevosDatos = scanner.scanCSVFile(filePath);
    List<Persona> nuevosDonantes = parser.parseCSVcontent(nuevosDatos);
    nuevosDonantes.forEach(registroDonantes::registrarDonante);
  }

}
