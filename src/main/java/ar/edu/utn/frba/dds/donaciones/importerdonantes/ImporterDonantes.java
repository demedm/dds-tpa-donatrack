package ar.edu.utn.frba.dds.donaciones.importerdonantes;

import ar.edu.utn.frba.dds.donaciones.donantes.RegistroDonante;
import ar.edu.utn.frba.dds.donaciones.donantes.Persona;

import java.util.List;

public class ImporterDonantes {
  private ImporterParser parser = new ImporterParser();
  private ImporterScanner scanner = new ImporterScanner();

  public void importarDonantes(String filePath, RegistroDonante registroDonantes) {
    List<String[]> nuevosDatos = scanner.scanCSVFile(filePath);
    List<Persona> nuevosDonantes = parser.parseCSVcontent(nuevosDatos);
    nuevosDonantes.forEach(registroDonantes::registrarDonante);
  }

  public ImporterParser getParser() {
    return this.parser;
  }

  public ImporterScanner getScanner() {
    return this.scanner;
  }

}
