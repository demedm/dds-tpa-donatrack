package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.donantes.PersonaFisica;
import ar.edu.utn.frba.dds.importerdonantes.ImporterDonantes;
import ar.edu.utn.frba.dds.medioscontacto.Mail;
import ar.edu.utn.frba.dds.medioscontacto.Telefono;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ar.edu.utn.frba.dds.importerdonantes.ImporterDonantes.registroDonantes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegistroDonanteTest {

  private RegistroDonante registro;

  @BeforeEach
  public void setup() {
    registro = new RegistroDonante();
    registroDonantes.clear();
  }
  @Test
  public void registrarNuevoDonante() {

    PersonaFisica donante =
        new PersonaFisica(
            new Mail("ana@mail.com"),
            "Ana",
            "Perez",
            "12345678",
            new Telefono("1122334455")
        );

    registro.registrarDonante(donante);

    assertEquals(1, registroDonantes.size());
  }
  @Test
  public void registrarDonanteExistenteNoDuplica() {

    PersonaFisica d1 =
        new PersonaFisica(
            new Mail("ana@mail.com"),
            "Ana",
            "Perez",
            "12345678",
            new Telefono("1122334455")
        );

    PersonaFisica d2 =
        new PersonaFisica(
            new Mail("ana@mail.com"),
            "Ana Maria",
            "Perez",
            "12345678",
            new Telefono("1122334455")
        );

    registro.registrarDonante(d1);
    registro.registrarDonante(d2);

    assertEquals(1, registroDonantes.size());
  }

  @Test
  public void registrarDonanteExistenteActualizaDatos() {

    PersonaFisica original =
        new PersonaFisica(
            new Mail("ana@mail.com"),
            "Ana",
            "Perez",
            "12345678",
            new Telefono("1122334455")
        );

    PersonaFisica actualizado =
        new PersonaFisica(
            new Mail("ana@mail.com"),
            "Ana Maria",
            "Perez",
            "12345678",
            new Telefono("1122334455")
        );

    registro.registrarDonante(original);
    registro.registrarDonante(actualizado);

    Donante encontrado = ImporterDonantes.buscarPorEmail("ana@mail.com");
    assertNotNull(encontrado);

    assertEquals("Ana Maria", encontrado.getNombre());
  }
}