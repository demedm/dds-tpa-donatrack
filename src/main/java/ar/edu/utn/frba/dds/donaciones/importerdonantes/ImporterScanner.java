package ar.edu.utn.frba.dds.donaciones.importerdonantes;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.List;

public class ImporterScanner {

  public List<String[]> scanCSVFile(String filePath) {
    List<String[]> filas;
    try { // scannea info en este formato: { [campo1, campo2], [hola, hola2], ... }
      var fileReader = new CSVReader(new FileReader(filePath));
      filas = fileReader.readAll();
    } catch(Exception e) {
      throw new RuntimeException("Error al leer el archivo CSV");
    }
    return filas;
  }

}
