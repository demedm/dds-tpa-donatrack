package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.medioscontacto.Telefono;
import ar.edu.utn.frba.dds.repositories.EntidadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EntidadTest {

  private EntidadRepository repository;

  @BeforeEach
  public void setup() {

    repository = new EntidadRepository();
  }

  private EntidadBeneficiaria entidad() {
    return new EntidadBeneficiaria(
        "Av. 9 de Julio 4827",
        List.of(new Mail("contacto@ninosfelices.org")),
        "Niños Felices",
        new Telefono("1122334455"),
        "ONG"
    );
  }

  @Test
  public void registrarAsignarID() {
    EntidadBeneficiaria entidad = entidad();

    repository.registrar(entidad);

    assertNotNull(entidad.getId());
  }


  @Test
  public void obtenerEntidadPorID() {
    EntidadBeneficiaria entidad = entidad();
    repository.registrar(entidad);

    EntidadBeneficiaria encontrada = repository.obtenerPorId(entidad.getId());

    assertEquals(entidad, encontrada);
  }

  @Test
  public void obtenerEntidadInexistente() {
    assertNull(repository.obtenerPorId("333111"));
  }

  @Test
  public void ObtenerTodasLasEntidades() {
    repository.registrar(entidad());
    repository.registrar(entidad());

    assertEquals(2, repository.obtenerEntidades().size());
  }

  @Test
  public void eliminarIDValidarExistncia() {
    EntidadBeneficiaria entidad = entidad();
    repository.registrar(entidad);

    boolean eliminado = repository.eliminarPorId(entidad.getId());

    assertTrue(eliminado);
    assertNull(repository.obtenerPorId(entidad.getId()));
  }

  @Test
  public void eliminarEntidadInexistente() {
    assertFalse(repository.eliminarPorId("999999"));
  }

  @Test
  public void ActualizarAlgunosCampoENtidad() {
    EntidadBeneficiaria existente = entidad();
    repository.registrar(existente);

    EntidadBeneficiaria camposNuevos = new EntidadBeneficiaria(
        "Colon 4156",
        List.of(new Mail("contacto@ninosfelices.org")),
        "Felices los Niños",
        new Telefono("1195733922"),
        "ONG"
    );

    EntidadBeneficiaria actualizada = repository.actualizarEntidad(existente, camposNuevos);

    assertEquals("Colon 4156", actualizada.getDireccion());
    assertEquals("1195733922", actualizada.getTelefono().getMedioContacto());
    assertEquals(List.of("contacto@ninosfelices.org"),
        actualizada.getMailsContacto().stream().map(Mail::getMedioContacto).toList());
    assertEquals("ONG", actualizada.getTipoEntidad());
  }
}
