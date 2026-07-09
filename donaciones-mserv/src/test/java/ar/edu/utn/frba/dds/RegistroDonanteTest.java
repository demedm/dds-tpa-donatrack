package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.model.donantes.Genero;
import ar.edu.utn.frba.dds.model.donantes.Identificacion;
import ar.edu.utn.frba.dds.model.donantes.PersonaFisica;
import ar.edu.utn.frba.dds.model.donantes.TipoDocumento;
import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.medioscontacto.Telefono;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistroDonanteTest {

  private RegistroDonante registro;

  @BeforeEach
  public void setup() {
    registro = new RegistroDonante();
  }

  @Test
  public void registrarNuevoDonante() {
    PersonaFisica donante = new PersonaFisica(
        new Mail("ana@mail.com"),
        "Ana Perez",
        new Identificacion(TipoDocumento.DNI, "12345678"),
        new Telefono("1122334455"),
        30,
        Genero.MUJER,
        "Calle Falsa 123"
    );

    registro.registrarDonante(donante);

    assertEquals(1, registro.getRegistroDonantes().size());
  }

  @Test
  public void registrarDonanteExistenteNoDuplica() {
    PersonaFisica d1 = new PersonaFisica(
        new Mail("ana@mail.com"),
        "Ana Perez",
        new Identificacion(TipoDocumento.DNI, "12345678"),
        new Telefono("1122334455"),
        30,
        Genero.MUJER,
        "Calle Falsa 123"
    );

    PersonaFisica d2 = new PersonaFisica(
        new Mail("ana@mail.com"),
        "Ana Maria Perez",
        new Identificacion(TipoDocumento.DNI, "12345678"),
        new Telefono("1122334455"),
        30,
        Genero.MUJER,
        "Calle Falsa 123"
    );

    registro.registrarDonante(d1);
    registro.registrarDonante(d2);

    assertEquals(1, registro.getRegistroDonantes().size());
  }

  @Test
  public void registrarDonanteExistenteActualizaDatos() {
    PersonaFisica original = new PersonaFisica(
        new Mail("ana@mail.com"),
        "Ana Perez",
        new Identificacion(TipoDocumento.DNI, "12345678"),
        new Telefono("1122334455"),
        30,
        Genero.MUJER,
        "Calle Falsa 123"
    );

    PersonaFisica actualizado = new PersonaFisica(
        new Mail("ana@mail.com"),
        "Ana Maria Perez",
        new Identificacion(TipoDocumento.DNI, "12345678"),
        new Telefono("1122334455"),
        30,
        Genero.MUJER,
        "Calle Falsa 123"
    );

    registro.registrarDonante(original);
    registro.registrarDonante(actualizado);

    String nombreEncontrado = registro.buscarPorEmail("ana@mail.com")
        .get()
        .getNombreIdentificador();

    assertEquals("Ana Maria Perez", nombreEncontrado);
  }
}
