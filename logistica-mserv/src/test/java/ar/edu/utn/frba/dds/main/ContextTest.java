package ar.edu.utn.frba.dds.main;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.usuarios.Chofer;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.CamionRepositorio;
import ar.edu.utn.frba.dds.repositories.RutaRepositorio;
import ar.edu.utn.frba.dds.repositories.UsuarioRepositorio;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ContextTest implements SimplePersistenceTest {
  CamionRepositorio repositorioCamion;
  UsuarioRepositorio repositorioUsuarios;
  RutaRepositorio repositorioRutas;

  @BeforeEach
  void setUp() {
    repositorioCamion = new CamionRepositorio();
    repositorioRutas = new RutaRepositorio();
    repositorioUsuarios = new UsuarioRepositorio();
  }

  @Test
  public void seRegistranLosCamiones() {
    Camion camion1 = new Camion("AAA BBB", 50000.0, 80000.0, 50.0);
    repositorioCamion.registrar(camion1);

    assertNotNull(camion1.getId());

    assertEquals(1, repositorioCamion.mostrarTodos().size());
  }

  @Test
  public void sePuedeBuscarCamionConId() {
    Camion camion1 = new Camion("AAA BBB", 50000.0, 80000.0, 50.0);
    Camion camion2 = new Camion("JK4 BBB", 50000.0, 80000.0, 50.0);
    Camion camion3 = new Camion("AAA OP2", 50000.0, 80000.0, 50.0);
    repositorioCamion.registrar(camion1);
    repositorioCamion.registrar(camion2);
    repositorioCamion.registrar(camion3);

    assertNotNull(camion1.getId());
    assertNotNull(camion2.getId());
    assertNotNull(camion3.getId());

    Camion camionBuscado = repositorioCamion.buscarPorId(camion3.getId());
    assertEquals(camion3, camionBuscado);
  }

  @Test
  public void seRegistranLosUsuarios() {
    Usuario usuario1 = new Usuario("Maria", "mariam@gmail.com");
    Chofer chofer1 = new Chofer();
    repositorioUsuarios.registrar(usuario1);

    assertNotNull(usuario1.getId());
    assertEquals(1, repositorioUsuarios.getAll().size());
  }

  @Test
  public void sePuedeBuscarUsuarioConId() {

  }

}
