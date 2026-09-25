package ar.edu.utn.frba.dds.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.EstadoCamion;
import ar.edu.utn.frba.dds.model.EstadoEntrega;
import ar.edu.utn.frba.dds.model.EstadoRuta;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.model.accionesentregas.AccionesSobreEntregas;
import ar.edu.utn.frba.dds.model.fallaentrega.ImprevistoLogistico;
import ar.edu.utn.frba.dds.model.usuarios.Chofer;
import ar.edu.utn.frba.dds.repositories.CamionRepositorio;
import ar.edu.utn.frba.dds.repositories.EntregaRepositorio;
import ar.edu.utn.frba.dds.repositories.RutaRepositorio;
import ar.edu.utn.frba.dds.repositories.UsuarioRepositorio;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// implementa persistencia para poder usar los repositorios
class CamionRutaEntregaTest implements SimplePersistenceTest {
  private Camion camion;
  private Chofer chofer;
  private Entrega entregaA;
  private Entrega entregaB;
  private List<Entrega> entregas;
  private Ruta ruta;
  private AccionesSobreEntregas accionMock;
  private CamionRepositorio camionRepositorio;
  private RutaRepositorio rutaRepositorio;
  private UsuarioRepositorio usuarioRepositorio;
  private EntregaRepositorio entregaRepositorio;

  @BeforeEach
  void setUp() {
    camionRepositorio = new CamionRepositorio();
    rutaRepositorio = new RutaRepositorio();
    usuarioRepositorio = new UsuarioRepositorio();
    entregaRepositorio = new EntregaRepositorio();
    chofer = new Chofer("carlitos@outlook.com", "Carlos", "Hola");
    camion = new Camion("AB123CD", 1000.0, 500.0, 220.0);

    entregaA = new Entrega("Calle Falsa 123", (long)1);
    entregaB = new Entrega("Av. Larga 742", (long)2);

    accionMock = mock(AccionesSobreEntregas.class);

    entregas = new ArrayList<>(List.of(entregaA, entregaB));
    ruta = new Ruta(entregas);
  }

  @Test
  void alCrearseElCamionEstaDisponible() {
    assertEquals(EstadoCamion.DISPONIBLE, camion.getEstado());
  }

  @Test
  void iniciarRutaCambiaEstadoDelCamion() {
    ruta.asignarCamion(camion);
    ruta.asignarChofer(chofer);
    assertEquals(EstadoCamion.RUTA_ASIGNADA, camion.getEstado());
    ruta.iniciarRuta();
    assertEquals(EstadoRuta.EN_CURSO, ruta.getEstado());
  }

  @Test
  void iniciarRutaPropagaElInicioATodasLasEntregas() {
    ruta.asignarCamion(camion);
    ruta.asignarChofer(chofer);
    ruta.iniciarRuta();

    assertEquals(EstadoCamion.REALIZANDO_ENTREGAS, camion.getEstado());
    assertEquals(EstadoEntrega.EN_TRASLADO, entregaA.getEstado());
    assertEquals(EstadoEntrega.EN_TRASLADO, entregaB.getEstado());
  }

  /*
  @Test
  public void marcarUnaEntregaComoEntregadaCambiaSuEstado() {
    ruta.asignarCamion(camion);
    ruta.iniciarRuta();


  }
   */

  @Test
  void regresarADepositoDejaAlCamionDisponibleYLaEntregaCambiaEstado() {
    ruta.asignarCamion(camion);
    ruta.asignarChofer(chofer);
    ruta.iniciarRuta();
    entregaB.marcarRegreso();
    camion.regresarDeposito();
    assertEquals(EstadoCamion.DISPONIBLE, camion.getEstado());
    // B regresa a deposito => vuelve a PENDIENTE
    assertEquals(EstadoEntrega.PENDIENTE, entregaB.getEstado());
  }

  @Test
  void improvistoLogisticoMarcaTodasLasEntregasDeLaRutaComoFallidasPorImprovisto() {
    usuarioRepositorio.registrar(chofer);
    entregaRepositorio.registrar(entregaA);
    entregaRepositorio.registrar(entregaB);
    camionRepositorio.registrar(camion);
    assertNotNull(camion.getId());
    assertEquals(1, camionRepositorio.mostrarTodos().size());
    rutaRepositorio.registrar(ruta);
    assertNotNull(ruta.getId());
    assertEquals(1, rutaRepositorio.mostrarTodos().size());

    ruta.asignarCamion(camion);
    ruta.asignarChofer(chofer);
    ruta.iniciarRuta();

    camionRepositorio.reportarImprevisto(camion.getId());

    assertEquals(EstadoEntrega.FALLIDA, entregaA.getEstado());
    assertEquals(EstadoEntrega.FALLIDA, entregaB.getEstado());
    assertInstanceOf(ImprevistoLogistico.class, entregaA.getMotivoFallo());
    assertInstanceOf(ImprevistoLogistico.class, entregaB.getMotivoFallo());
  }

  @Test
  void unaEntregaVencidaSeMarcaComoFallidaConMotivoEntregaVencida() {
    entregaA.setFechaVencimiento(LocalDate.now().minusDays(1));

    boolean resultado = entregaA.estaVencida();

    assertTrue(resultado);
    assertEquals(EstadoEntrega.FALLIDA, entregaA.getEstado());
    assertInstanceOf(
        ar.edu.utn.frba.dds.model.fallaentrega.EntregaVencida.class,
        entregaA.getMotivoFallo()
    );
  }

}