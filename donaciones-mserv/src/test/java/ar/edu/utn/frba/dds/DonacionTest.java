package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.Categoria;
import ar.edu.utn.frba.dds.model.Bienes.EstadoUso;
import ar.edu.utn.frba.dds.model.Bienes.PorEstado;
import ar.edu.utn.frba.dds.model.Bienes.PorVencimiento;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Bienes.UnidadMedida;
import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.donantes.PersonaFisica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DonacionTest {

  private Subcategoria subFideos;
  private Subcategoria subSillas;
  private Persona donante;
  private LocalDate vencimiento;

  @BeforeEach
  public void setUp() {
    subFideos = new Subcategoria(Categoria.ALIMENTOS, "fideos", UnidadMedida.UNIDADES);
    subSillas = new Subcategoria(Categoria.MOBILIARIO, "sillas", UnidadMedida.UNIDADES);
    donante = new PersonaFisica();
    vencimiento = LocalDate.of(2027, 1, 1);
  }

  // ---------- Segmentación ----------

  @Test
  public void bienesDeDistintaSubcategoriaGeneranVariasSegmentadas() {
    Donacion donacion = new Donacion("desc", List.of(fideos(3, vencimiento), silla(2, EstadoUso.USADO)), donante);

    assertEquals(2, donacion.getSegmentadas().size());
  }

  @Test
  public void bienesConMismaSubcategoriaYMismoVencimientoVanALaMismaSegmentada() {
    Donacion donacion = new Donacion("desc", List.of(fideos(3, vencimiento), fideos(4, vencimiento)), donante);

    assertEquals(1, donacion.getSegmentadas().size());
    assertEquals(0, new BigDecimal("7").compareTo(donacion.getSegmentadas().get(0).getCantidad()));
  }

  @Test
  public void bienesConMismaSubcategoriaYDistintoVencimientoVanASegmentadasDistintas() {
    Donacion donacion = new Donacion("desc",
        List.of(fideos(3, vencimiento), fideos(4, vencimiento.plusMonths(1))), donante);

    assertEquals(2, donacion.getSegmentadas().size());
  }

  @Test
  public void bienesConMismaSubcategoriaYDistintoEstadoDeUsoVanASegmentadasDistintas() {
    Donacion donacion = new Donacion("desc",
        List.of(silla(1, EstadoUso.NUEVO), silla(2, EstadoUso.USADO)), donante);

    assertEquals(2, donacion.getSegmentadas().size());
  }

  @Test
  public void laCantidadTotalSeConservaAlSegmentar() {
    Donacion donacion = new Donacion("desc", List.of(
        fideos(3, vencimiento),
        fideos(4, vencimiento),
        fideos(1, vencimiento.plusMonths(1)),
        silla(2, EstadoUso.USADO)), donante);

    BigDecimal total = BigDecimal.ZERO;
    for (DonacionSegmentada segmentada : donacion.getSegmentadas()) {
      total = total.add(segmentada.getCantidad());
    }

    assertEquals(3, donacion.getSegmentadas().size());
    assertEquals(0, new BigDecimal("10").compareTo(total));
  }

  // ---------- Datos que la segmentada calcula ----------

  @Test
  public void laSegmentadaConoceSuSubcategoriaYSuDonante() {
    Donacion donacion = new Donacion("desc", List.of(fideos(3, vencimiento)), donante);
    DonacionSegmentada segmentada = donacion.getSegmentadas().get(0);

    assertEquals(subFideos, segmentada.getSubcategoria());
    assertSame(donante, segmentada.getDonante());
  }

  @Test
  public void laSegmentadaNaceEnDeposito() {
    Donacion donacion = new Donacion("desc", List.of(fideos(3, vencimiento)), donante);

    assertTrue(donacion.getSegmentadas().get(0).estaAlmacen());
  }

  // ---------- Validaciones ----------

  @Test
  public void noSePuedeCrearUnaDonacionSinBienes() {
    assertThrows(IllegalArgumentException.class, () -> new Donacion("desc", List.of(), donante));
  }

  @Test
  public void noSePuedeCrearUnaDonacionSinDonante() {
    assertThrows(IllegalArgumentException.class,
        () -> new Donacion("desc", List.of(fideos(3, vencimiento)), null));
  }

  @Test
  public void unBienNoPuedeTenerCantidadCero() {
    assertThrows(IllegalArgumentException.class, () -> fideos(0, vencimiento));
  }

  @Test
  public void unBienNoPuedeCrearseSinCriterio() {
    assertThrows(IllegalArgumentException.class,
        () -> new Bien(subFideos, null, "fideos", new BigDecimal("3"), null));  }

  // ---------- Helpers ----------

  private Bien fideos(int cantidad, LocalDate vence) {
    return new Bien(subFideos, null, "fideos", BigDecimal.valueOf(cantidad), new PorVencimiento(vence));  }

  private Bien silla(int cantidad, EstadoUso estado) {
    return new Bien(subSillas, null, "silla", BigDecimal.valueOf(cantidad), new PorEstado(estado));  }
}
