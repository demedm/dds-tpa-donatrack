package ar.edu.utn.frba.dds.main;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.repositories.CamionRepositorio;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ContextTest implements SimplePersistenceTest {
  CamionRepositorio repositorio;

  @BeforeEach
  void setUp() {
    repositorio = new CamionRepositorio();
  }

  @Test
  public void seRegistranLosCamiones() {
    Camion camion1 = new Camion("AAA BBB", 50000.0, 80000.0, 50.0);
    repositorio.registrar(camion1);

    assertNotNull(camion1.getId());

    assertEquals(1, repositorio.getAll().size());
  }
}
