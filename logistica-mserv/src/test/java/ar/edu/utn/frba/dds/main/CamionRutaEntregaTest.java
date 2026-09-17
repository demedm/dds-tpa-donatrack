package ar.edu.utn.frba.dds.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class CamionRutaEntregaTest {
  private Camion camion;
  private Entrega entregaA;
  private Entrega entregaB;
  private List<Entrega> entregas;
  private Ruta ruta;
  private AccionesSobreEntregas accionMock;

  @BeforeEach
  void setUp() {
    Chofer chofer = new Chofer("Carlos", "Hola");
    camion = new Camion("AB123CD", 1000.0, 500.0, 220.0);

    entregaA = new Entrega("Calle Falsa 123", (long)1);
    entregaB = new Entrega("Av. Larga 742", (long)2);

    accionMock = mock(AccionesSobreEntregas.class);
    entregaA.agregarAccionEntregas(accionMock);
    entregaB.agregarAccionEntregas(accionMock);

    entregas = new ArrayList<>(List.of(entregaA, entregaB));
    ruta = new Ruta(chofer, entregas);
  }

  @Test
  void alCrearseElCamionEstaDisponible() {
    assertEquals(EstadoCamion.DISPONIBLE, camion.getEstado());
  }

  @Test
  void iniciarRutaCambiaEstadoDelCamion() {
    ruta.setCamion(camion);
    assertEquals(EstadoCamion.RUTA_ASIGNADA, camion.getEstado());
    ruta.iniciarRuta();
    assertEquals(EstadoRuta.EN_CURSO, ruta.getEstado());
  }

  @Test
  void iniciarRutaPropagaElInicioATodasLasEntregasYNotifica() {
    ruta.setCamion(camion);
    ruta.iniciarRuta();

    assertEquals(EstadoCamion.REALIZANDO_ENTREGAS, camion.getEstado());
    assertEquals(EstadoEntrega.EN_TRASLADO, entregaA.getEstado());
    assertEquals(EstadoEntrega.EN_TRASLADO, entregaB.getEstado());

    verify(accionMock, times(1)).notificarInicioRuta(entregaA);
    verify(accionMock, times(1)).notificarInicioRuta(entregaB);
  }

  @Test
  void visitarParadaMarcaSoloLaEntregaDeEsaDireccionComoEntregada() {
    ruta.setCamion(camion);
    ruta.iniciarRuta();

    ruta.visitarParada("Calle Falsa 123");

    assertTrue(entregaA.getEntregado());
    assertEquals(EstadoEntrega.ENTREGADA, entregaA.getEstado());

    // La otra entrega de la ruta no se ve afectada
    assertFalse(entregaB.getEntregado());
    assertEquals(EstadoEntrega.EN_TRASLADO, entregaB.getEstado());
  }

  @Test
  void regresarADepositoDejaAlCamionDisponibleYRegresarEntregasADepositoCambiaEstado() {
    ruta.setCamion(camion);
    ruta.iniciarRuta();

    ruta.visitarParada("Calle Falsa 123");
    entregaB.marcarRegreso();

    camion.regresarDeposito();

    assertEquals(EstadoCamion.DISPONIBLE, camion.getEstado());

    // A fue visitada
    assertEquals(EstadoEntrega.ENTREGADA, entregaA.getEstado());

    // B regresa a deposito => vuelve a PENDIENTE
    assertEquals(EstadoEntrega.PENDIENTE, entregaB.getEstado());
  }

  @Test
  void improvistoLogisticoMarcaTodasLasEntregasDeLaRutaComoFallidasPorImprovisto() {
    ruta.setCamion(camion);
    ruta.iniciarRuta();

    ruta.indicarImprovistoLogistico();

    assertEquals(EstadoEntrega.FALLIDA, entregaA.getEstado());
    assertEquals(EstadoEntrega.FALLIDA, entregaB.getEstado());
    assertInstanceOf(ImprevistoLogistico.class, entregaA.getMotivoFallo());
    assertInstanceOf(ImprevistoLogistico.class, entregaB.getMotivoFallo());

    // Se notificó el fallo para cada entrega
    verify(accionMock, times(1)).notificarFalloEntrega(entregaA);
    verify(accionMock, times(1)).notificarFalloEntrega(entregaB);
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