package ar.edu.utn.frba.dds.main;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.model.accionesentregas.AccionesSobreEntregas;
import ar.edu.utn.frba.dds.model.usuarios.Administrador;
import ar.edu.utn.frba.dds.model.usuarios.Chofer;
import ar.edu.utn.frba.dds.model.usuarios.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.repositories.CamionRepositorio;
import ar.edu.utn.frba.dds.repositories.EntregaRepositorio;
import ar.edu.utn.frba.dds.repositories.RutaRepositorio;
import ar.edu.utn.frba.dds.repositories.UsuarioRepositorio;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ContextTest implements SimplePersistenceTest {
  CamionRepositorio repositorioCamion;
  UsuarioRepositorio repositorioUsuarios;
  RutaRepositorio repositorioRutas;
  EntregaRepositorio repositorioEntregas;

  @BeforeEach
  void setUp() {
    repositorioEntregas = new EntregaRepositorio();
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
  public void sePuedeEliminarUnCamion() {
    Camion camion1 = new Camion("AAA BBB", 50000.0, 80000.0, 50.0);
    repositorioCamion.registrar(camion1);
    assertNotNull(camion1.getId());
    assertEquals(1, repositorioCamion.mostrarTodos().size());

    repositorioCamion.eliminarCamion(camion1);
    assertEquals(0, repositorioCamion.mostrarTodos().size());
  }

  @Test
  public void seRegistranLosUsuarios() {
    Administrador maria = new Administrador("mariam@gmail.com");
    repositorioUsuarios.registrar(maria);

    assertNotNull(maria.getId());
    assertEquals(1, repositorioUsuarios.mostrarTodos().size());
  }

  @Test
  public void sePuedeBuscarUsuarioConId() {
    Chofer chofer1 = new Chofer("chofer@outlook.com", "Carlos", "Mandala");
    EntidadBeneficiaria entidad = new EntidadBeneficiaria("entidad@gmail.com", "Av Sta Fe", "Caritas");
    repositorioUsuarios.registrar(entidad);
    repositorioUsuarios.registrar(chofer1);
    assertNotNull(chofer1.getId());
    assertNotNull(chofer1.getId());

    Chofer choferBuscado = repositorioUsuarios.buscarChoferPorId(chofer1.getId());
    EntidadBeneficiaria entidadBuscada = repositorioUsuarios.buscarEntidadBeneficiariaPorId(entidad.getId());
    assertEquals(chofer1, choferBuscado);
    assertEquals(entidad, entidadBuscada);
  }

  @Test
  public void sePuedeEliminarUnUsuario() {
    Administrador maria = new Administrador("mariam@gmail.com");
    Chofer chofer1 = new Chofer("chofer@outlook.com", "Carlos", "Mandala");
    repositorioUsuarios.registrar(maria);
    repositorioUsuarios.registrar(chofer1);
    assertNotNull(chofer1.getId());
    assertNotNull(maria.getId());
    assertEquals(2, repositorioUsuarios.mostrarTodos().size());

    repositorioUsuarios.eliminarUsuario(maria);
    repositorioUsuarios.eliminarUsuario(chofer1);
    assertEquals(0, repositorioCamion.mostrarTodos().size());
  }

  @Test
  public void seRegistranLasRutas() {
    Entrega entregaA = new Entrega("Calle Falsa 123", (long)1);
    Entrega entregaB = new Entrega("Av. Larga 742", (long)2);
    var entregas = new ArrayList<>(List.of(entregaA, entregaB));
    Chofer chofer = new Chofer("chofer@outlook.com", "Carlos", "Mandala");
    Ruta ruta = new Ruta(entregas);
    entregas.forEach(repositorioEntregas::registrar);
    repositorioUsuarios.registrar(chofer);
    repositorioRutas.registrar(ruta);
    assertNotNull(ruta.getId());
    assertEquals(1, repositorioRutas.mostrarTodos().size());
    assertEquals(2, repositorioEntregas.mostrarTodos().size());
  }

  @Test
  public void sePuedeBuscarRutaPorId() {
    Entrega entregaA = new Entrega("Calle Falsa 123", (long)1);
    Entrega entregaB = new Entrega("Av. Larga 742", (long)2);
    var entregas = new ArrayList<>(List.of(entregaA, entregaB));
    Chofer chofer = new Chofer("chofer@outlook.com", "Carlos", "Mandala");
    Ruta ruta = new Ruta(entregas);
    entregas.forEach(repositorioEntregas::registrar);
    repositorioUsuarios.registrar(chofer);
    repositorioRutas.registrar(ruta);
    assertNotNull(ruta.getId());

    Ruta rutaBuscada = repositorioRutas.buscarPorId(ruta.getId());
    assertEquals(ruta, rutaBuscada);
  }

  @Test
  public void seMuestranLasEntregasDeUnaRuta() {
    Entrega entregaA = new Entrega("Calle Falsa 123", (long)1);
    Entrega entregaB = new Entrega("Av. Larga 742", (long)2);
    var entregas = new ArrayList<>(List.of(entregaA, entregaB));
    Chofer chofer = new Chofer("chofer@outlook.com", "Carlos", "Mandala");
    Ruta ruta = new Ruta(entregas);
    entregas.forEach(repositorioEntregas::registrar);
    repositorioUsuarios.registrar(chofer);
    repositorioRutas.registrar(ruta);
    assertNotNull(ruta.getId());

    var entregasBuscadas = repositorioEntregas.buscarEntregasDeRuta(ruta.getId());
    assertEquals(entregas, entregasBuscadas);
  }

  @Test
  public void sePuedeEliminarUnaRutaConSusEntregas() {
    Entrega entregaA = new Entrega("Calle Falsa 123", (long)1);
    Entrega entregaB = new Entrega("Av. Larga 742", (long)2);
    var entregas = new ArrayList<>(List.of(entregaA, entregaB));
    Chofer chofer = new Chofer("chofer@outlook.com", "Carlos", "Mandala");
    Ruta ruta = new Ruta(entregas);
    entregas.forEach(repositorioEntregas::registrar);
    repositorioUsuarios.registrar(chofer);
    repositorioRutas.registrar(ruta);
    assertNotNull(ruta.getId());
    assertEquals(1, repositorioRutas.mostrarTodos().size());

    repositorioRutas.eliminarRuta(ruta);
    assertEquals(0, repositorioRutas.mostrarTodos().size());
    assertEquals(2, repositorioEntregas.mostrarTodos().size());
  }

}
