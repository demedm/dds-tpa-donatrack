package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.model.donantes.Genero;
import ar.edu.utn.frba.dds.model.donantes.Identificacion;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.donantes.PersonaFisica;
import ar.edu.utn.frba.dds.model.donantes.PersonaJuridica;
import ar.edu.utn.frba.dds.model.donantes.TipoDocumento;
import ar.edu.utn.frba.dds.model.donantes.TipoEntidadJuridica;
import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.medioscontacto.Telefono;
import ar.edu.utn.frba.dds.repositories.DonanteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DonanteTest {

  private DonanteRepository repository;

  @BeforeEach
  public void setup() {

    repository = new DonanteRepository();
  }

  private PersonaFisica personaFisica(String email) {
    PersonaFisica persona = new PersonaFisica();
    persona.setNombreIdentificador("Ana Losada");
    persona.setMail(new Mail(email));
    persona.setMedioPreferido(persona.getMail());
    persona.setTelefono(new Telefono("1122334455"));
    persona.setIdentificacion(new Identificacion(TipoDocumento.DNI, "12345678"));
    persona.setEdad(30);
    persona.setGenero(Genero.MUJER);
    persona.setDireccionActual("Calle Falsa 123");
    return persona;
  }

  private PersonaJuridica personaJuridica(String email) {
    PersonaJuridica persona = new PersonaJuridica();
    persona.setNombreIdentificador("Patitas");
    persona.setMail(new Mail(email));
    persona.setMedioPreferido(persona.getMail());
    persona.setTelefono(new Telefono("1144556677"));
    persona.setIdentificacion(new Identificacion(TipoDocumento.CUIT, "30712345671"));
    persona.setRubro("alimenticio");
    persona.setTipo(TipoEntidadJuridica.ONG);
    return persona;
  }

  private void registrar(Persona persona) {
    try {
      repository.registrarDonante(persona);
    } catch (RuntimeException e) {

    }
  }

  @Test
  public void registrarNuevoDonanteLoAgregaAlRegistro() {
    registrar(personaFisica("ana@mail.com"));

    assertEquals(1, repository.getRegistroDonantes().size());
  }

  @Test
  public void buscarPorEmailEncuentraAlDonanteRegistrado() {
    registrar(personaFisica("analosada@mail.com"));

    Optional<Persona> encontrado = repository.buscarPorEmail("analosada@mail.com");

    assertTrue(encontrado.isPresent());
    assertEquals("Ana Losada", encontrado.get().getNombreIdentificador());
  }

  @Test
  public void buscarPorEmailDevuelveVacioSiNoExiste() {
    assertTrue(repository.buscarPorEmail("nadie@mail.com").isEmpty());
  }

  @Test
  public void eliminarDonante() {
    registrar(personaFisica("anagomez@mail.com"));

    boolean eliminado = repository.eliminarPorEmail("anagomez@mail.com");

    assertTrue(eliminado);
    assertTrue(repository.buscarPorEmail("anagomez@mail.com").isEmpty());
  }

  @Test
  public void eliminarPorEmailDevuelveFalseSiNoExiste() {
    assertFalse(repository.eliminarPorEmail("nadie@mail.com"));
  }

  @Test
  public void registrarConMismoEmailActualiza() {
    registrar(personaFisica("analosada@mail.com"));

    PersonaFisica actualizada = personaFisica("analosada@mail.com");
    actualizada.setNombreIdentificador("Ana Losada");
    registrar(actualizada);

    assertEquals(2, repository.getRegistroDonantes().size());
    assertEquals("Ana Losada",
        repository.buscarPorEmail("camila.martinez@mail.com").get().getNombreIdentificador());
  }

  @Test
  public void actualizarPersonaFisica() {
    PersonaFisica existente = personaFisica("camila.martinez@mail.com");
    PersonaFisica nuevosDatos = personaFisica("camila.martinez@mail.com");
    nuevosDatos.setNombreIdentificador("Camila Martinez");
    nuevosDatos.setEdad(31);
    nuevosDatos.setGenero(Genero.OTRO);
    nuevosDatos.setDireccionActual("Calle Nueva 456");
    nuevosDatos.setTelefono(new Telefono("1199998888"));

    existente.actualizarInfo(nuevosDatos);

    assertEquals("Camila Martinez", existente.getNombreIdentificador());
    assertEquals(31, existente.getEdad());
    assertEquals(Genero.OTRO, existente.getGenero());
    assertEquals("Calle Nueva 456", existente.getDireccionActual());
    assertEquals("1199998888", existente.getTelefono().getMedioContacto());
  }

  @Test
  public void actualizarPersonaJuridica() {
    PersonaJuridica existente = personaJuridica("patitas@organimales.com");
    PersonaJuridica nuevosDatos = personaJuridica("patitas@organimales.com");
    nuevosDatos.setRubro("salud");
    nuevosDatos.setTipo(TipoEntidadJuridica.EMPRESA);

    existente.actualizarInfo(nuevosDatos);

    assertEquals("salud", existente.getRubro());
    assertEquals(TipoEntidadJuridica.EMPRESA, existente.getTipo());
  }
}
