package ar.edu.utn.frba.dds.Matchmaking;

import ar.edu.utn.frba.dds.model.Asignacion.AlgoritmoAsignacion;
import ar.edu.utn.frba.dds.model.Asignacion.Resultados;
import ar.edu.utn.frba.dds.model.Bienes.Categoria;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.tareas.ProcesarDonaciones;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

      Resultados resultados = donacion.buscarCandidatas(entidades, List.of(algoritmoA, algoritmoB));

      assertEquals(List.of(hogar), resultados.entidadesPropuestas());
    }

    @Test
    void siNoHayCoincidenciasProponeLasDeTodosLosAlgoritmos() {
      when(algoritmoA.obtenerRanking(any(), any())).thenReturn(List.of(comedor));
      when(algoritmoB.obtenerRanking(any(), any())).thenReturn(List.of(club));

      List<EntidadBeneficiaria> propuestas =
          donacion.buscarCandidatas(entidades, List.of(algoritmoA, algoritmoB)).entidadesPropuestas();

      assertEquals(2, propuestas.size());
      assertTrue(propuestas.containsAll(List.of(comedor, club)));
    }

    @Test
    void funcionaConCualquierCantidadDeAlgoritmos() {
      AlgoritmoAsignacion algoritmoC = mock(AlgoritmoAsignacion.class);
      when(algoritmoA.obtenerRanking(any(), any())).thenReturn(List.of(comedor, hogar, club));
      when(algoritmoB.obtenerRanking(any(), any())).thenReturn(List.of(hogar, club));
      when(algoritmoC.obtenerRanking(any(), any())).thenReturn(List.of(club));

      Resultados resultados = donacion.buscarCandidatas(entidades, List.of(algoritmoA, algoritmoB, algoritmoC));

      assertEquals(List.of(club), resultados.entidadesPropuestas());
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

      Resultados resultados = donacion.buscarCandidatas(entidades, List.of(algoritmoA));

      assertSame(resultados, donacion.getResultadosPropuestos());
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

      assertEquals(List.of(hogar), arroz.getResultadosPropuestos().entidadesPropuestas());
      assertEquals(List.of(hogar), fideos.getResultadosPropuestos().entidadesPropuestas());
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

      assertNotNull(sana.getResultadosPropuestos());
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

  private static EntidadBeneficiaria entidad(String nombre) {
    return new EntidadBeneficiaria("Calle falsa 123", new ArrayList<>(), nombre, null, "COMEDOR");
  }

  private static DonacionSegmentada donacionEnDeposito(String subcategoria) {
    return new DonacionSegmentada(10, new Subcategoria(Categoria.ALIMENTOS, subcategoria), null);
  }
}