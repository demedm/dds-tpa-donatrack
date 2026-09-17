package ar.edu.utn.frba.dds.server;

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
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.model.accionesentregas.AccionesSobreEntregas;
import ar.edu.utn.frba.dds.model.fallaentrega.ImprevistoLogistico;
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
    camion = new Camion("AB123CD", 1000, 500, 220);

    entregaA = new Entrega("Calle Falsa 123", 1);
    entregaB = new Entrega("Av. Larga 742", 2);

    accionMock = mock(AccionesSobreEntregas.class);
    entregaA.agregarAccionEntregas(accionMock);
    entregaB.agregarAccionEntregas(accionMock);

    entregas = new ArrayList<>(List.of(entregaA, entregaB));
    ruta = new Ruta(camion.getPatente(), entregas);
  }

  @Test
  void alCrearseElCamionEstaDisponible() {
    assertEquals(EstadoCamion.DISPONIBLE, camion.getEstado());
    assertNull(camion.getRutaActual());
  }

  @Test
  void asignarRutaCambiaEstadoDelCamionYLeAsignaLaRuta() {
    camion.asignarRuta(ruta);

    assertEquals(EstadoCamion.RUTA_ASIGNADA, camion.getEstado());
    assertEquals(ruta, camion.getRutaActual());
  }

  @Test
  void iniciarRutaPropagaElInicioATodasLasEntregasYNotifica() {
    camion.asignarRuta(ruta);
    camion.iniciarRuta();

    assertEquals(EstadoCamion.REALIZANDO_ENTREGAS, camion.getEstado());
    assertEquals(EstadoEntrega.EN_TRASLADO, entregaA.getEstado());
    assertEquals(EstadoEntrega.EN_TRASLADO, entregaB.getEstado());

    verify(accionMock, times(1)).notificarInicioRuta(entregaA);
    verify(accionMock, times(1)).notificarInicioRuta(entregaB);
  }

  @Test
  void visitarParadaMarcaSoloLaEntregaDeEsaDireccionComoEntregada() {
    camion.asignarRuta(ruta);
    camion.iniciarRuta();

    ruta.visitarParada("Calle Falsa 123");

    assertTrue(entregaA.getVisitado());
    assertEquals(EstadoEntrega.ENTREGADA, entregaA.getEstado());

    // La otra entrega de la ruta no se ve afectada
    assertFalse(entregaB.getVisitado());
    assertEquals(EstadoEntrega.EN_TRASLADO, entregaB.getEstado());
  }

  @Test
  void regresarADepositoDejaAlCamionDisponibleYReintegraEntregasNoVisitadasConFallo() {
    camion.asignarRuta(ruta);
    camion.iniciarRuta();

    ruta.visitarParada("Calle Falsa 123");       // A se entrega
    entregaB.marcarComoNoRecepcionada();          // B falla (no recepcionada)

    camion.regresarADeposito();

    assertEquals(EstadoCamion.DISPONIBLE, camion.getEstado());

    // A fue visitada, entonces no se reintegra a PENDIENTE
    assertEquals(EstadoEntrega.ENTREGADA, entregaA.getEstado());

    // B no fue visitada y tiene motivo de fallo => vuelve a PENDIENTE
    assertEquals(EstadoEntrega.PENDIENTE, entregaB.getEstado());
  }

  @Test
  void improvistoLogisticoMarcaTodasLasEntregasDeLaRutaComoFallidasPorImprovisto() {
    camion.asignarRuta(ruta);
    camion.iniciarRuta();

    camion.improvistoLogistico();

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