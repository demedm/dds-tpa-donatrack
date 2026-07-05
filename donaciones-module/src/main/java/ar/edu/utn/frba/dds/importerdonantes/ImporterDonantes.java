package ar.edu.utn.frba.dds.importerdonantes;

import ar.edu.utn.frba.dds.RegistroDonante;
import ar.edu.utn.frba.dds.donantes.Identificacion;
import ar.edu.utn.frba.dds.donantes.Persona;
import ar.edu.utn.frba.dds.medioscontacto.MedioContacto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
