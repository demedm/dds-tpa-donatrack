package ar.edu.utn.frba.dds.Persistencia;

import ar.edu.utn.frba.dds.model.Asignacion.AlgoritmoAsignacion;
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
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.necesidad.Necesidad;
import ar.edu.utn.frba.dds.model.necesidad.Peticion;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;
import ar.edu.utn.frba.dds.repositories.EntidadRepository;
import ar.edu.utn.frba.dds.tareas.ProcesarDonaciones;
import ar.edu.utn.frba.dds.model.Estado.EnTraslado;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PersistenciaDonacionesTest implements WithSimplePersistenceUnit {

  private final DonacionesRepository repo = DonacionesRepository.Instance;
  private final List<Long> donacionesCreadas = new ArrayList<>();
  private final List<String> entidadesCreadas = new ArrayList<>();

  private Subcategoria arroz;
  private Subcategoria silla;
  private Persona donante;

  // Subcategoria y Persona no se guardan en cascada desde Bien/Donacion:
  // son catálogo y datos que ya existen, así que se persisten antes
  @BeforeEach
  void setUp() {
    arroz = new Subcategoria(Categoria.ALIMENTOS, "ARROZ", UnidadMedida.UNIDADES);
    silla = new Subcategoria(Categoria.MOBILIARIO, "SILLA", UnidadMedida.UNIDADES);
    donante = new PersonaFisica();
    donante.setNombreIdentificador("Donante de prueba");

    withTransaction(() -> {
      entityManager().persist(arroz);
      entityManager().persist(silla);
      entityManager().persist(donante);
    });
  }

  @AfterEach
  void borrarLoCreado() {
    donacionesCreadas.forEach(repo::eliminar);
    entidadesCreadas.forEach(EntidadRepository.Instance::eliminarPorId);
  }

  // ---------- Donación y segmentadas ----------

  @Test
  void seGuardaLaDonacionConSusSegmentadas() {
    Donacion leida = guardarYReleer(donacionDeArrozYSilla());

    assertNotNull(leida);
    assertEquals("Donación de prueba", leida.getDescripcionGeneral());
    assertEquals(2, leida.getSegmentadas().size());
  }

  @Test
  void laDonacionVuelveConSuDonante() {
    Donacion leida = guardarYReleer(donacionDeArrozYSilla());

    assertEquals(donante.getId(), leida.getDonante().getId());
  }

  @Test
  void laSegmentadaVuelveConSuSubcategoriaYLaSumaDeSusBienes() {
    Donacion leida = guardarYReleer(donacionDeArrozYSilla());
    DonacionSegmentada segmentadaArroz = segmentadaDe(leida, arroz);

    assertEquals(arroz, segmentadaArroz.getSubcategoria());
    assertEquals(2, segmentadaArroz.getBienes().size());
    assertEquals(15, segmentadaArroz.getCantidad());
  }

  @Test
  void eliminarLaDonacionBorraTambienSusSegmentadas() {
    Donacion donacion = donacionDeArrozYSilla();
    guardar(donacion);
    Long idSegmentada = donacion.getSegmentadas().get(0).getId();

    repo.eliminar(donacion.getId());
    donacionesCreadas.remove(donacion.getId());
    entityManager().clear();

    assertNull(repo.findById(donacion.getId()));
    assertNull(repo.findSegmentadaById(idSegmentada));
  }

  // ---------- Bienes y criterio de segmentación ----------

  @Test
  void elCriterioPorVencimientoVuelveConSuFecha() {
    Donacion leida = guardarYReleer(donacionDeArrozYSilla());

    Bien bien = segmentadaDe(leida, arroz).getBienes().get(0);

    assertInstanceOf(PorVencimiento.class, bien.getCriterio());
    assertEquals(vencimiento(), ((PorVencimiento) bien.getCriterio()).getFechaVencimiento());
  }

  @Test
  void elCriterioPorEstadoVuelveConSuEstadoDeUso() {
    Donacion leida = guardarYReleer(donacionDeArrozYSilla());

    Bien bien = segmentadaDe(leida, silla).getBienes().get(0);

    assertInstanceOf(PorEstado.class, bien.getCriterio());
    assertEquals(EstadoUso.NUEVO, bien.getCriterioDeAgrupacion().criterio());
  }

  // ---------- Estados ----------

  @Test
  void persistirYRecuperarEstadoDeDonacion() {
    Donacion donacion = donacionDeArrozYSilla();
    DonacionSegmentada segmentada = segmentadaDe(donacion, arroz);

    // nace En depósito y al pasar a En traslado se agrega al historial
    segmentada.setEstado(new EnTraslado());
    guardar(donacion);
    entityManager().clear();

    DonacionSegmentada recuperada = repo.findSegmentadaById(segmentada.getId());

    assertEquals("En traslado", recuperada.getEstadoActual().getNombre());
    assertEquals(2, recuperada.getHistorialEstados().size());
    assertEquals("En traslado", recuperada.getHistorialEstados().get(1).getEstado());
  }

  @Test
  void soloTraeLasSegmentadasQueSiguenEnDeposito() {
    Donacion donacion = donacionDeArrozYSilla();
    DonacionSegmentada asignada = segmentadaDe(donacion, silla);

    asignada.asignar();
    guardar(donacion);
    entityManager().clear();

    List<Long> enDeposito = repo.findSegmentadasEnDeposito().stream()
        .map(DonacionSegmentada::getId).toList();

    assertTrue(enDeposito.contains(segmentadaDe(donacion, arroz).getId()));
    assertFalse(enDeposito.contains(asignada.getId()));
  }

  // ---------- Matchmaking ----------

  @Test
  void lasPropuestasDelMatchmakingQuedanGuardadas() {
    Donacion donacion = donacionDeArrozYSilla();
    guardar(donacion);
    Long idSegmentada = segmentadaDe(donacion, arroz).getId();

    EntidadBeneficiaria comedor = entidad("ENT-TEST-1");
    AlgoritmoAsignacion algoritmo = mock(AlgoritmoAsignacion.class);
    when(algoritmo.obtenerRanking(any(), any())).thenReturn(List.of(comedor));

    withTransaction(() ->
        repo.findSegmentadaById(idSegmentada).buscarCandidatas(List.of(comedor), List.of(algoritmo)));
    entityManager().clear();

    assertEquals(List.of("ENT-TEST-1"), repo.findSegmentadaById(idSegmentada).getIdsEntidadesPropuestas());
  }

  @Test
  void elCronDejaLasPropuestasEnLaBase() {
    Donacion donacion = donacionDeArrozYSilla();
    guardar(donacion);
    Long idSegmentada = segmentadaDe(donacion, arroz).getId();

    // el comedor necesita arroz, así los dos algoritmos lo proponen
    EntidadBeneficiaria comedor = entidad(null);
    Necesidad necesidad = new Necesidad();
    necesidad.agregarPeticion(new Peticion("ARROZ", 100));
    comedor.getNecesidades().add(necesidad);
    EntidadRepository.Instance.registrar(comedor);
    entidadesCreadas.add(comedor.getId());

    ProcesarDonaciones.ejecutar();
    entityManager().clear();

    assertTrue(repo.findSegmentadaById(idSegmentada).getIdsEntidadesPropuestas().contains(comedor.getId()));
  }

  // ---------- Helpers ----------

  // 2 bienes de arroz con el mismo vencimiento (una segmentada) y una silla (otra segmentada)
  private Donacion donacionDeArrozYSilla() {
    List<Bien> bienes = new ArrayList<>(List.of(
        new Bien(arroz, "arroz.jpg", "Arroz 1kg", 10, new PorVencimiento(vencimiento())),
        new Bien(arroz, "arroz.jpg", "Arroz 1kg", 5, new PorVencimiento(vencimiento())),
        new Bien(silla, "silla.jpg", "Silla de madera", 1, new PorEstado(EstadoUso.NUEVO))));

    return new Donacion("Donación de prueba", bienes, donante);
  }

  private static LocalDate vencimiento() {
    return LocalDate.of(2027, 1, 1);
  }

  // El id lo genera la base al guardar: recién ahí lo anoto para borrarlo después
  private void guardar(Donacion donacion) {
    repo.guardar(donacion);
    donacionesCreadas.add(donacion.getId());
  }

  private Donacion guardarYReleer(Donacion donacion) {
    guardar(donacion);
    entityManager().clear();
    return repo.findById(donacion.getId());
  }

  private static DonacionSegmentada segmentadaDe(Donacion donacion, Subcategoria subcategoria) {
    return donacion.getSegmentadas().stream()
        .filter(s -> s.getSubcategoria().equals(subcategoria))
        .findFirst()
        .orElseThrow(() -> new AssertionError("No hay segmentada de " + subcategoria.getDescripcion()));
  }

  private static EntidadBeneficiaria entidad(String id) {
    EntidadBeneficiaria entidad =
        new EntidadBeneficiaria("Calle falsa 123", new ArrayList<>(), "Comedor de prueba", null, "COMEDOR");

    if (id != null) entidad.setId(id);
    return entidad;
  }
}
