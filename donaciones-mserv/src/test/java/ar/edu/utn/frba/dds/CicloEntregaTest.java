package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.Categoria;
import ar.edu.utn.frba.dds.model.Bienes.PorVencimiento;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Bienes.UnidadMedida;
import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.Estado.RegistroCambioEstado;
import ar.edu.utn.frba.dds.model.donantes.PersonaFisica;
import ar.edu.utn.frba.dds.model.necesidad.Necesidad;
import ar.edu.utn.frba.dds.model.necesidad.Peticion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// El ciclo de una donación asignada y lo que ve la petición en cada paso:
// al asignar se compromete, al entregar se recibe, si falla se libera
class CicloEntregaTest {

  private static final Long COMEDOR = 1L;

  private Peticion arroz50;
  private DonacionSegmentada donacion30;

  @BeforeEach
  void setUp() {
    arroz50 = new Peticion("ARROZ", new BigDecimal("50"));
    donacion30 = segmentadaDeArroz("30");
  }

  @Test
  void alAsignarLaCantidadQuedaComprometidaPeroNoRecibida() {
    donacion30.asignarA(COMEDOR, arroz50);

    assertCantidad("30", arroz50.getCantidadComprometida());
    assertCantidad("0", arroz50.getCantidadRecibida());
    assertFalse(arroz50.estaCubierta());
    assertSame(arroz50, donacion30.getPeticionCubierta());
  }

  @Test
  void alEntregarseLaCantidadPasaDeComprometidaARecibida() {
    entregar(donacion30, arroz50);

    assertCantidad("0", arroz50.getCantidadComprometida());
    assertCantidad("30", arroz50.getCantidadRecibida());
    assertFalse(arroz50.estaCubierta(), "Pidió 50 y llegaron 30: faltan 20");
  }

  @Test
  void conVariasEntregasLaPeticionQuedaCubierta() {
    entregar(donacion30, arroz50);
    entregar(segmentadaDeArroz("20"), arroz50);

    assertCantidad("50", arroz50.getCantidadRecibida());
    assertTrue(arroz50.estaCubierta());
  }

  @Test
  void siLaEntregaFallaLaPeticionNoCuentaNadaYLaDonacionQuedaLibre() {
    donacion30.asignarA(COMEDOR, arroz50);
    donacion30.planificarRuta();
    donacion30.iniciarTraslado();

    donacion30.fallarEntrega("Tocamos timbre pero nadie respondió");

    assertCantidad("0", arroz50.getCantidadComprometida());
    assertCantidad("0", arroz50.getCantidadRecibida());
    assertTrue(donacion30.estaAlmacen());
    assertNull(donacion30.getEntidadAsignadaId());
    assertNull(donacion30.getPeticionCubierta());
  }

  @Test
  void laEntregaFallidaQuedaEnElHistorial() {
    donacion30.asignarA(COMEDOR, arroz50);
    donacion30.planificarRuta();
    donacion30.iniciarTraslado();
    donacion30.fallarEntrega("Nadie respondió");

    List<String> estados = donacion30.getHistorialEstados().stream()
        .map(RegistroCambioEstado::getEstado)
        .toList();
    assertEquals(List.of("En depósito", "Asignación realizada", "Lista para entregar",
        "En traslado", "Entrega fallida", "En depósito"), estados);
  }

  @Test
  void siLogisticaLaDevuelvePendienteTambienSeLibera() {
    donacion30.asignarA(COMEDOR, arroz50);
    donacion30.planificarRuta();

    donacion30.cambiarEstado("PENDIENTE");

    assertCantidad("0", arroz50.getCantidadComprometida());
    assertTrue(donacion30.estaAlmacen());
    assertNull(donacion30.getPeticionCubierta());
  }

  @Test
  void loQueVieneEnCaminoCuentaParaNoAsignarDeMas() {
    Peticion arroz30 = new Peticion("ARROZ", new BigDecimal("30"));
    DonacionSegmentada otra = segmentadaDeArroz("30");

    donacion30.asignarA(COMEDOR, arroz30);

    assertFalse(arroz30.faltaAsignar(), "Ya viene en camino todo lo que pidió");
    assertFalse(otra.puedeCubrir(arroz30));
  }

  @Test
  void unaEntregaNoSeConfirmaDosVecesNiSeSumaDosVeces() {
    entregar(donacion30, arroz50);

    assertThrows(IllegalStateException.class, donacion30::confirmarEntrega);
    assertCantidad("30", arroz50.getCantidadRecibida());
  }

  @Test
  void noSeConfirmaUnaEntregaQueNoSalioDelDeposito() {
    donacion30.asignarA(COMEDOR, arroz50);

    assertThrows(IllegalStateException.class, donacion30::confirmarEntrega);
    assertCantidad("0", arroz50.getCantidadRecibida());
    assertCantidad("30", arroz50.getCantidadComprometida());
  }

  @Test
  void sinPeticionQueCubrirSeAsignaIgual() {
    donacion30.asignarA(COMEDOR, null);
    donacion30.planificarRuta();
    donacion30.iniciarTraslado();
    donacion30.confirmarEntrega();

    assertEquals(COMEDOR, donacion30.getEntidadAsignadaId());
    assertNull(donacion30.getPeticionCubierta());
  }

  @Test
  void laNecesidadEstaCubiertaCuandoTodasSusPeticionesLoEstan() {
    Peticion fideos10 = new Peticion("FIDEOS", new BigDecimal("10"));
    Necesidad necesidad = new Necesidad();
    necesidad.agregarPeticion(arroz50);
    necesidad.agregarPeticion(fideos10);

    entregar(segmentadaDeArroz("50"), arroz50);
    assertFalse(necesidad.estaCubierta(), "Falta fideos");

    entregar(segmentada("FIDEOS", "10"), fideos10);
    assertTrue(necesidad.estaCubierta());
  }

  // ---------- helpers ----------

  private static void entregar(DonacionSegmentada donacion, Peticion peticion) {
    donacion.asignarA(COMEDOR, peticion);
    donacion.planificarRuta();
    donacion.iniciarTraslado();
    donacion.confirmarEntrega();
  }

  private static DonacionSegmentada segmentadaDeArroz(String cantidad) {
    return segmentada("ARROZ", cantidad);
  }

  private static DonacionSegmentada segmentada(String subcategoria, String cantidad) {
    Subcategoria sub = new Subcategoria(Categoria.ALIMENTOS, subcategoria, UnidadMedida.KILOGRAMOS);
    Bien bien = new Bien(sub, null, subcategoria, new BigDecimal(cantidad),
        new PorVencimiento(LocalDate.now().plusMonths(6)));
    return new Donacion("Donación de prueba", List.of(bien), new PersonaFisica()).getSegmentadas().get(0);
  }

  private static void assertCantidad(String esperada, BigDecimal real) {
    assertEquals(0, new BigDecimal(esperada).compareTo(real),
        "Se esperaba " + esperada + " pero fue " + real);
  }
}
