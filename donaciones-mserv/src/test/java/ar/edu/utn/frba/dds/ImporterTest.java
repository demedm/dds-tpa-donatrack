package ar.edu.utn.frba.dds;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.utn.frba.dds.model.donantes.Identificacion;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.donantes.TipoDocumento;
import ar.edu.utn.frba.dds.model.donantes.TipoPersona;
import ar.edu.utn.frba.dds.model.importerdonantes.ImporterDonantes;
import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;
import ar.edu.utn.frba.dds.model.medioscontacto.Telefono;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class ImporterTest {
  /*

  private ImporterDonantes importer;
  private List<Persona> registroPrevio = new ArrayList<>();
  private String filePathCSV = "src/main/java/ar/edu/utn/frba/dds/donantes_import_20000_UTF8_BOM.csv";

  @BeforeEach
  void initRegistroPrevio() {
    // Persona JURIDICA a actualizar
    Mail mail1 = new Mail("romero5314@yahoo.com"); // dato viejo
    Identificacion id1 = new Identificacion(TipoDocumento.CUIT, "30-52235350-3");
    Persona santaFeIndustrial = new Persona(
        "Santa Fe Industrial", mail1, id1, TipoPersona.JURIDICA);
    registroPrevio.add(santaFeIndustrial);

    // Persona a agregar 1
    Mail mail2 = new Mail("yocooperativa8180@yahoo.com");
    Telefono telefono2 = new Telefono("+54 11 4724-2300");
    Identificacion id2 = new Identificacion(TipoDocumento.CUIT, "12312355");
    Persona contacto = new Persona("Yo Cooperativa", mail2, id2, TipoPersona.JURIDICA);
    contacto.setTelefono(telefono2);
    registroPrevio.add(contacto);

    // Persona a agregar 2
    Mail mail3 = new Mail("abcd@gmail.com");
    Identificacion id3 = new Identificacion(TipoDocumento.DNI, "45645688");
    Persona mariaDolores = new Persona("Maria Dolores", mail3, id3, TipoPersona.FISICA);
    registroPrevio.add(mariaDolores);

    // Persona FISICA a actualizar
    Mail mail4 = new Mail("maríaaguirre4360@outlook.com");
    MedioContacto telefono = new Telefono("+54 11 4848-0000"); // dato viejo
    Identificacion id4 = new Identificacion(TipoDocumento.DNI, "49018895");
    Persona mariaAguirre = new Persona("María Aguirre", mail4,
        id4, TipoPersona.FISICA);
    registroPrevio.add(mariaAguirre);
  }

  @BeforeEach
  void initImporter() {
    importer = new ImporterDonantes();
  }

  @Test
  public void scanneaCorrectamentedelCSV() {
    List<String[]> filas = new ArrayList<>();
    try {
      filas = importer.getScanner().scanCSVFile(filePathCSV);
    } catch (RuntimeException e) {
      throw new RuntimeException(e);
    }
    assertEquals(filas.size(), 2001);
  }

  /*
  @Test
  void parseaCorrectamenteUnaFila() {
      List<String[]> filas = List.of(
          new String[]{
              "PERSONA HUMANA",
              "DNI",
              "12345678",
              "Juan Perez",
              "juan@gmail.com",
              "+54 11 4444-4444"
          }
      );

      List<Persona> personas = importer.getParser().parseCSVcontent(filas);

      assertEquals(1, personas.size());

      Persona persona = personas.getFirst();

      assertEquals("Juan Perez", persona.getNombre());
      assertEquals(TipoPersona.FISICA, persona.getTipoPersona());
      assertEquals(TipoDocumento.DNI, persona.getIdentificacion().getTipo());
      assertEquals("12345678", persona.getIdentificacion().getNumero());
  }
  */

}
