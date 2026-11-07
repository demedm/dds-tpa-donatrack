package ar.edu.utn.frba.dds.model.importerdonantes;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

  public List<String[]> scanCSVFile(InputStream inputStream) {
    try (CSVReader reader = new CSVReader(
        new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

      return reader.readAll();

    } catch (Exception e) {
      throw new RuntimeException("Error al leer el archivo CSV", e);
    }
  }

}
