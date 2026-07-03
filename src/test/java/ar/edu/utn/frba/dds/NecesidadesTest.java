package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.necesidad.NecesidadRecurrente;
import ar.edu.utn.frba.dds.necesidad.NecesidadDominio.Necesidad;
import ar.edu.utn.frba.dds.necesidad.NecesidadDominio.NecesidadRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NecesidadesTest {
  private Necesidad necesidad;
  private NecesidadRecurrente necesidadRecurrente;
  private NecesidadRepository gestor;

  @BeforeEach
  public void setup() {
    necesidad = new Necesidad();
    necesidadRecurrente = new NecesidadRecurrente(7);
    gestor = new NecesidadRepository();
  }

  @Test
  public void unaNecesidadNuevaEstaEnPreparacion() {
    assertEquals("preparacion", necesidad.getEstado());
  }

  @Test
  public void agregarPeticionALaLista() {
    necesidad.agregarPeticiones("arroz", 5);
    assertEquals(1, necesidad.getPeticiones().size());
  }

  @Test
  public void agregarVariasPeticionesALaLista() {
    necesidad.agregarPeticiones("arroz", 5);
    necesidad.agregarPeticiones("fideos", 3);
    assertEquals(2, necesidad.getPeticiones().size());
  }

  @Test
  public void pedidoListoCambiaEstado() {
    necesidad.pedidoListo();
    assertTrue(necesidad.estaPreparado());
  }

  @Test
  public void necesidadRecurrenteGuardaDias() {
    assertEquals(7, necesidadRecurrente.getDiasAvencer());
  }

  public void gestorAgregaNecesidades() {
    gestor.necesidades.add(necesidad);
    gestor.necesidades.add(necesidadRecurrente);
    assertEquals(2, gestor.necesidades.size());
  }
}