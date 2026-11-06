package ar.edu.utn.frba.dds.Matchmaking;

import ar.edu.utn.frba.dds.model.Asignacion.AlgoritmoAsignacion;
import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.Categoria;
import ar.edu.utn.frba.dds.model.Bienes.PorVencimiento;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Bienes.UnidadMedida;
import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.donantes.PersonaFisica;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.tareas.ProcesarDonaciones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class AsignacionDonacionesTest {

  private EntidadBeneficiaria comedor;
  private EntidadBeneficiaria hogar;
  private EntidadBeneficiaria club;
  private List<EntidadBeneficiaria> entidades;
  private AlgoritmoAsignacion algoritmoA;
  private AlgoritmoAsignacion algoritmoB;

  @BeforeEach
  void setUp() {
    comedor = entidad("Comedor");
    hogar = entidad("Hogar");
    club = entidad("Club");
    entidades = List.of(comedor, hogar, club);
    algoritmoA = mock(AlgoritmoAsignacion.class);
    algoritmoB = mock(AlgoritmoAsignacion.class);
    when(algoritmoA.nombre()).thenReturn("A");
    when(algoritmoB.nombre()).thenReturn("B");
  }

  // ---------- Una donacion: combinar los rankings ----------

  @Nested
  class BuscarCandidatas {

    private DonacionSegmentada donacion;

    @BeforeEach
    void setUp() {
      donacion = donacionEnDeposito("ARROZ");
    }

    @Test
    void proponeSoloLasEntidadesQueAparecenEnTodosLosAlgoritmos() {
      when(algoritmoA.obtenerRanking(any(), any())).thenReturn(List.of(comedor, hogar));
      when(algoritmoB.obtenerRanking(any(), any())).thenReturn(List.of(hogar, club));

      donacion.buscarCandidatas(entidades, List.of(algoritmoA, algoritmoB));

      assertEquals(List.of(hogar.getId()), donacion.getIdsEntidadesPropuestas());
    }

    @Test
    void siNoHayCoincidenciasProponeLasDeTodosLosAlgoritmos() {
      when(algoritmoA.obtenerRanking(any(), any())).thenReturn(List.of(comedor));
      when(algoritmoB.obtenerRanking(any(), any())).thenReturn(List.of(club));

      donacion.buscarCandidatas(entidades, List.of(algoritmoA, algoritmoB));

      assertFalse(donacion.huboCoincidencias());
      assertEquals(List.of(comedor.getId(), club.getId()), donacion.getIdsEntidadesPropuestas());
    }

    @Test
    void funcionaConCualquierCantidadDeAlgoritmos() {
      AlgoritmoAsignacion algoritmoC = mock(AlgoritmoAsignacion.class);
      when(algoritmoA.obtenerRanking(any(), any())).thenReturn(List.of(comedor, hogar, club));
      when(algoritmoB.obtenerRanking(any(), any())).thenReturn(List.of(hogar, club));
      when(algoritmoC.obtenerRanking(any(), any())).thenReturn(List.of(club));

      donacion.buscarCandidatas(entidades, List.of(algoritmoA, algoritmoB, algoritmoC));

      assertEquals(List.of(club.getId()), donacion.getIdsEntidadesPropuestas());
      assertEquals(3, donacion.getEjecuciones().size());
    }

    @Test
    void leConsultaACadaAlgoritmoPorEstaDonacion() {
      when(algoritmoA.obtenerRanking(any(), any())).thenReturn(List.of());
      when(algoritmoB.obtenerRanking(any(), any())).thenReturn(List.of());

      donacion.buscarCandidatas(entidades, List.of(algoritmoA, algoritmoB));

      verify(algoritmoA).obtenerRanking(donacion, entidades);
      verify(algoritmoB).obtenerRanking(donacion, entidades);
    }

    @Test
    void dejaElResultadoGuardadoEnLaDonacion() {
      when(algoritmoA.obtenerRanking(any(), any())).thenReturn(List.of(hogar));

      donacion.buscarCandidatas(entidades, List.of(algoritmoA));

      assertEquals(List.of(hogar.getId()), donacion.getIdsEntidadesPropuestas());
    }

    @Test
    void guardaElRankingDeCadaAlgoritmoEnOrden() {
      when(algoritmoA.obtenerRanking(any(), any())).thenReturn(List.of(club, comedor));
      when(algoritmoB.obtenerRanking(any(), any())).thenReturn(List.of(hogar));

      donacion.buscarCandidatas(entidades, List.of(algoritmoA, algoritmoB));

      assertEquals("A", donacion.getEjecuciones().get(0).getAlgoritmo());
      assertEquals(List.of(club.getId(), comedor.getId()), donacion.getEjecuciones().get(0).getIdsEntidades());
      assertEquals("B", donacion.getEjecuciones().get(1).getAlgoritmo());
      assertEquals(List.of(hogar.getId()), donacion.getEjecuciones().get(1).getIdsEntidades());
    }

    @Test
    void volverACorrerElMatchmakingReemplazaLasEjecucionesAnteriores() {
      when(algoritmoA.obtenerRanking(any(), any())).thenReturn(List.of(comedor));
      donacion.buscarCandidatas(entidades, List.of(algoritmoA, algoritmoB));

      when(algoritmoA.obtenerRanking(any(), any())).thenReturn(List.of(club));
      donacion.buscarCandidatas(entidades, List.of(algoritmoA));

      assertEquals(1, donacion.getEjecuciones().size());
      assertEquals(List.of(club.getId()), donacion.getIdsEntidadesPropuestas());
    }

    @Test
    void noSeBuscanCandidatasParaUnaDonacionQueYaNoEstaEnDeposito() {
      donacion.asignar();   // pasa a ASIGNACION_REALIZADA

      assertThrows(IllegalStateException.class,
          () -> donacion.buscarCandidatas(entidades, List.of(algoritmoA)));
      verifyNoInteractions(algoritmoA);
    }
  }

  //todas las donaciones

  @Nested
  class TareaProcesarDonaciones {

    @BeforeEach
    void setUp() {
      when(algoritmoA.obtenerRanking(any(), any())).thenReturn(List.of(hogar));
    }

    @Test
    void todasLasDonacionesQuedanConPropuestas() {
      DonacionSegmentada arroz = donacionEnDeposito("ARROZ");
      DonacionSegmentada fideos = donacionEnDeposito("FIDEOS");

      ProcesarDonaciones.procesar(List.of(arroz, fideos), entidades, List.of(algoritmoA));

      assertEquals(List.of(hogar.getId()), arroz.getIdsEntidadesPropuestas());
      assertEquals(List.of(hogar.getId()), fideos.getIdsEntidadesPropuestas());
    }

    @Test
    void sinDonacionesNoCorreNingunAlgoritmo() {
      ProcesarDonaciones.procesar(List.of(), entidades, List.of(algoritmoA));

      verifyNoInteractions(algoritmoA);
    }

    @Test
    void unaDonacionQueFallaNoFrenaALasDemas() {
      DonacionSegmentada rota = donacionEnDeposito("ARROZ");
      DonacionSegmentada sana = donacionEnDeposito("FIDEOS");
      when(algoritmoA.obtenerRanking(eq(rota), any())).thenThrow(new RuntimeException("ranking roto"));

      assertThrows(RuntimeException.class,
          () -> ProcesarDonaciones.procesar(List.of(rota, sana), entidades, List.of(algoritmoA)));

      assertEquals(List.of(hogar.getId()), sana.getIdsEntidadesPropuestas());
    }

    @Test
    void elErrorDiceQueDonacionFallo() {
      DonacionSegmentada rota = donacionEnDeposito("ARROZ");
      when(algoritmoA.obtenerRanking(eq(rota), any())).thenThrow(new RuntimeException("ranking roto"));

      RuntimeException error = assertThrows(RuntimeException.class,
          () -> ProcesarDonaciones.procesar(List.of(rota), entidades, List.of(algoritmoA)));

      assertTrue(error.getMessage().contains("1 donacion(es)"));
      assertTrue(error.getMessage().contains("ranking roto"));
    }
  }

  // ---------- helpers ----------

  private static long proximoId = 1;

  private static EntidadBeneficiaria entidad(String nombre) {
    EntidadBeneficiaria entidad = new EntidadBeneficiaria("Calle falsa 123", new ArrayList<>(), nombre, null, "COMEDOR");
    entidad.setId(proximoId++);
    return entidad;
  }

  // La segmentada la arma la donación al segmentar sus bienes, y nace En depósito
  private static DonacionSegmentada donacionEnDeposito(String subcategoria) {
    Subcategoria sub = new Subcategoria(Categoria.ALIMENTOS, subcategoria, UnidadMedida.UNIDADES);
    Bien bien = new Bien(sub, null, subcategoria, new BigDecimal("10"), new PorVencimiento(LocalDate.now().plusMonths(6)));    return new Donacion("Donación de prueba", List.of(bien), new PersonaFisica()).getSegmentadas().get(0);
  }
}