package ar.edu.utn.frba.dds.Persistencia;

import ar.edu.utn.frba.dds.model.Asignacion.AlgoritmoAsignacion;
import ar.edu.utn.frba.dds.model.Asignacion.AlgoritmoDeCompatibilidad;
import ar.edu.utn.frba.dds.model.Asignacion.AlgoritmoSubatendidos;
import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.Categoria;
import ar.edu.utn.frba.dds.model.Bienes.EstadoUso;
import ar.edu.utn.frba.dds.model.Bienes.PorEstado;
import ar.edu.utn.frba.dds.model.Bienes.PorVencimiento;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Bienes.UnidadMedida;
import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.Estado.EnTraslado;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.donantes.PersonaFisica;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.necesidad.Necesidad;
import ar.edu.utn.frba.dds.model.necesidad.Peticion;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;
import ar.edu.utn.frba.dds.repositories.EntidadRepository;
import ar.edu.utn.frba.dds.tareas.ProcesarDonaciones;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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

// Cada test corre dentro de una transacción que SimplePersistenceTest deshace al terminar:
// no hace falta borrar nada a mano y los tests no se pisan entre sí
public class PersistenciaDonacionesTest implements SimplePersistenceTest {

  private final DonacionesRepository repo = DonacionesRepository.Instance;

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

    entityManager().persist(arroz);
    entityManager().persist(silla);
    entityManager().persist(donante);
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
    assertEquals(0, new BigDecimal("15").compareTo(segmentadaArroz.getCantidad()));
  }

  @Test
  void eliminarLaDonacionBorraTambienSusSegmentadas() {
    Donacion donacion = donacionDeArrozYSilla();
    repo.guardar(donacion);
    Long idSegmentada = donacion.getSegmentadas().get(0).getId();

    repo.eliminar(donacion.getId());
    sincronizarYLimpiar();

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
    repo.guardar(donacion);
    sincronizarYLimpiar();

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
    repo.guardar(donacion);
    sincronizarYLimpiar();

    List<Long> enDeposito = repo.findSegmentadasEnDeposito().stream()
        .map(DonacionSegmentada::getId).toList();

    assertTrue(enDeposito.contains(segmentadaDe(donacion, arroz).getId()));
    assertFalse(enDeposito.contains(asignada.getId()));
  }

  // ---------- Matchmaking ----------

  @Test
  void lasPropuestasDelMatchmakingQuedanGuardadas() {
    Donacion donacion = donacionDeArrozYSilla();
    repo.guardar(donacion);
    Long idSegmentada = segmentadaDe(donacion, arroz).getId();

    EntidadBeneficiaria comedor = entidad(1000L);
    AlgoritmoAsignacion algoritmo = mock(AlgoritmoAsignacion.class);
    when(algoritmo.obtenerRanking(any(), any())).thenReturn(List.of(comedor));

    repo.findSegmentadaById(idSegmentada).buscarCandidatas(List.of(comedor), List.of(algoritmo));
    sincronizarYLimpiar();

    assertEquals(List.of(1000L), repo.findSegmentadaById(idSegmentada).getIdsEntidadesPropuestas());
  }

  @Test
  void lasEjecucionesDeCadaAlgoritmoVuelvenDeLaBaseEnOrden() {
    Donacion donacion = donacionDeArrozYSilla();
    repo.guardar(donacion);
    Long idSegmentada = segmentadaDe(donacion, arroz).getId();

    AlgoritmoAsignacion compatibilidad = mock(AlgoritmoAsignacion.class);
    AlgoritmoAsignacion subatendidos = mock(AlgoritmoAsignacion.class);
    when(compatibilidad.nombre()).thenReturn("COMPATIBILIDAD");
    when(subatendidos.nombre()).thenReturn("SUBATENDIDOS");
    when(compatibilidad.obtenerRanking(any(), any())).thenReturn(List.of(entidad(3000L), entidad(1000L), entidad(2000L)));
    when(subatendidos.obtenerRanking(any(), any())).thenReturn(List.of(entidad(2000L), entidad(4000L)));

    repo.findSegmentadaById(idSegmentada).buscarCandidatas(List.of(), List.of(compatibilidad, subatendidos));
    sincronizarYLimpiar();

    DonacionSegmentada leida = repo.findSegmentadaById(idSegmentada);
    assertEquals(2, leida.getEjecuciones().size());
    assertEquals("COMPATIBILIDAD", leida.getEjecuciones().get(0).getAlgoritmo());
    assertEquals(List.of(3000L, 1000L, 2000L), leida.getEjecuciones().get(0).getIdsEntidades());
    assertEquals("SUBATENDIDOS", leida.getEjecuciones().get(1).getAlgoritmo());
    assertEquals(List.of(2000L, 4000L), leida.getEjecuciones().get(1).getIdsEntidades());
    assertEquals(List.of(2000L), leida.getCoincidencias());
  }

  @Test
  void volverACorrerElMatchmakingBorraLasEjecucionesViejasDeLaBase() {
    Donacion donacion = donacionDeArrozYSilla();
    repo.guardar(donacion);
    Long idSegmentada = segmentadaDe(donacion, arroz).getId();

    AlgoritmoAsignacion algoritmo = mock(AlgoritmoAsignacion.class);
    when(algoritmo.nombre()).thenReturn("COMPATIBILIDAD");
    when(algoritmo.obtenerRanking(any(), any())).thenReturn(List.of(entidad(1000L)));
    repo.findSegmentadaById(idSegmentada).buscarCandidatas(List.of(), List.of(algoritmo, algoritmo));
    sincronizarYLimpiar();

    when(algoritmo.obtenerRanking(any(), any())).thenReturn(List.of(entidad(2000L)));
    repo.findSegmentadaById(idSegmentada).buscarCandidatas(List.of(), List.of(algoritmo));
    sincronizarYLimpiar();

    assertEquals(1, repo.findSegmentadaById(idSegmentada).getEjecuciones().size());
    assertEquals(List.of(2000L), repo.findSegmentadaById(idSegmentada).getIdsEntidadesPropuestas());
    // contando solo las de esta segmentada (otros tests pueden dejar datos commiteados)
    Number filas = (Number) entityManager()
        .createNativeQuery("select count(*) from ejecuciones_algoritmo where donacion_segmentada_id = ?1")
        .setParameter(1, idSegmentada)
        .getSingleResult();
    assertEquals(1, filas.intValue(), "Las ejecuciones viejas tienen que borrarse (orphanRemoval)");
  }

  @Test
  void elProcesoDeMatchmakingDejaLasPropuestasEnLaBase() {
    Donacion donacion = donacionDeArrozYSilla();
    repo.guardar(donacion);
    Long idSegmentada = segmentadaDe(donacion, arroz).getId();

    // el comedor necesita arroz, así los dos algoritmos lo proponen
    EntidadBeneficiaria comedor = entidad(null);
    Necesidad necesidad = new Necesidad();
    necesidad.agregarPeticion(new Peticion("ARROZ", new BigDecimal("100")));
    comedor.agregarNecesidad(necesidad);
    entityManager().persist(comedor);
    comedor.getNecesidades().forEach(n -> n.setEntidadId(comedor.getId()));

    // Se usa procesar() y no ejecutar(): ejecutar() abre su propia transacción y haría
    // commit de la del test. La tarea completa (con Quartz) se prueba en AsignacionProgramadaTest
    ProcesarDonaciones.procesar(
        repo.findSegmentadasEnDeposito(),
        EntidadRepository.Instance.obtenerEntidades(),
        List.of(new AlgoritmoDeCompatibilidad(), new AlgoritmoSubatendidos()));
    sincronizarYLimpiar();

    assertTrue(repo.findSegmentadaById(idSegmentada).getIdsEntidadesPropuestas().contains(comedor.getId()));
  }

  // ---------- Asignación y entrega: la petición ----------

  @Test
  void laDonacionAsignadaRecuerdaSuPeticionYLaCantidadComprometida() {
    Donacion donacion = donacionDeArrozYSilla();
    repo.guardar(donacion);
    Long idSegmentada = segmentadaDe(donacion, arroz).getId();   // 2 bienes: 10 + 5
    Peticion peticion = peticionPersistida("ARROZ", "100");

    repo.findSegmentadaById(idSegmentada).asignarA(entidadPersistidaId(), peticion);
    sincronizarYLimpiar();

    DonacionSegmentada leida = repo.findSegmentadaById(idSegmentada);
    assertNotNull(leida.getPeticionCubierta());
    assertEquals(peticion.getId(), leida.getPeticionCubierta().getId());
    assertEquals(0, new BigDecimal("15").compareTo(leida.getPeticionCubierta().getCantidadComprometida()));
    assertEquals(0, BigDecimal.ZERO.compareTo(leida.getPeticionCubierta().getCantidadRecibida()));
  }

  @Test
  void alConfirmarseLaEntregaLaPeticionGuardaLoRecibido() {
    Donacion donacion = donacionDeArrozYSilla();
    repo.guardar(donacion);
    Long idSegmentada = segmentadaDe(donacion, arroz).getId();
    Peticion peticion = peticionPersistida("ARROZ", "100");

    DonacionSegmentada segmentada = repo.findSegmentadaById(idSegmentada);
    segmentada.asignarA(entidadPersistidaId(), peticion);
    segmentada.planificarRuta();
    segmentada.iniciarTraslado();
    sincronizarYLimpiar();

    // como llega el aviso de logística: se busca por id y se cambia el estado
    repo.findSegmentadaById(idSegmentada).cambiarEstado("ENTREGADA");
    sincronizarYLimpiar();

    Peticion leida = entityManager().find(Peticion.class, peticion.getId());
    assertEquals(0, new BigDecimal("15").compareTo(leida.getCantidadRecibida()));
    assertEquals(0, BigDecimal.ZERO.compareTo(leida.getCantidadComprometida()));
  }

  @Test
  void siLaEntregaFallaLaBaseQuedaSinAsignacion() {
    Donacion donacion = donacionDeArrozYSilla();
    repo.guardar(donacion);
    Long idSegmentada = segmentadaDe(donacion, arroz).getId();
    Peticion peticion = peticionPersistida("ARROZ", "100");

    DonacionSegmentada segmentada = repo.findSegmentadaById(idSegmentada);
    segmentada.asignarA(entidadPersistidaId(), peticion);
    segmentada.planificarRuta();
    segmentada.iniciarTraslado();
    sincronizarYLimpiar();

    DonacionSegmentada enTraslado = repo.findSegmentadaById(idSegmentada);
    enTraslado.setJustificacionFallo("Nadie respondió");
    enTraslado.cambiarEstado("FALLIDA");
    sincronizarYLimpiar();

    DonacionSegmentada leida = repo.findSegmentadaById(idSegmentada);
    assertTrue(leida.estaAlmacen());
    assertNull(leida.getPeticionCubierta());
    assertNull(leida.getEntidadAsignadaId());
    Peticion peticionLeida = entityManager().find(Peticion.class, peticion.getId());
    assertEquals(0, BigDecimal.ZERO.compareTo(peticionLeida.getCantidadComprometida()));
    assertEquals(0, BigDecimal.ZERO.compareTo(peticionLeida.getCantidadRecibida()));
  }

  // ---------- Helpers ----------

  // 2 bienes de arroz con el mismo vencimiento (una segmentada) y una silla (otra segmentada)
  private Donacion donacionDeArrozYSilla() {
    List<Bien> bienes = new ArrayList<>(List.of(
        new Bien(arroz, "arroz.jpg", "Arroz 1kg", new BigDecimal("10"), new PorVencimiento(vencimiento())),
        new Bien(arroz, "arroz.jpg", "Arroz 1kg", new BigDecimal("5"), new PorVencimiento(vencimiento())),
        new Bien(silla, "silla.jpg", "Silla de madera", BigDecimal.ONE, new PorEstado(EstadoUso.NUEVO))));

    return new Donacion("Donación de prueba", bienes, donante);
  }

  private static LocalDate vencimiento() {
    return LocalDate.of(2027, 1, 1);
  }

  private Donacion guardarYReleer(Donacion donacion) {
    repo.guardar(donacion);
    sincronizarYLimpiar();
    return repo.findById(donacion.getId());
  }

  // flush: manda a la base lo pendiente (dentro de la transacción del test)
  // clear: olvida los objetos en memoria, para que lo siguiente se lea DE LA BASE
  // entidadAsignadaId tiene FK a entidades_beneficiarias: la entidad tiene que existir
  private Long entidadPersistidaId() {
    EntidadBeneficiaria comedor = entidad(null);
    entityManager().persist(comedor);
    return comedor.getId();
  }

  private Peticion peticionPersistida(String subclase, String cantidad) {
    Necesidad necesidad = new Necesidad();
    Peticion peticion = new Peticion(subclase, new BigDecimal(cantidad));
    necesidad.agregarPeticion(peticion);
    entityManager().persist(necesidad);   // cascade ALL: guarda también la petición
    return peticion;
  }

  private void sincronizarYLimpiar() {
    entityManager().flush();
    entityManager().clear();
  }

  private static DonacionSegmentada segmentadaDe(Donacion donacion, Subcategoria subcategoria) {
    return donacion.getSegmentadas().stream()
        .filter(s -> s.getSubcategoria().equals(subcategoria))
        .findFirst()
        .orElseThrow(() -> new AssertionError("No hay segmentada de " + subcategoria.getDescripcion()));
  }

  private static EntidadBeneficiaria entidad(Long id) {
    EntidadBeneficiaria entidad =
        new EntidadBeneficiaria("Calle falsa 123", new ArrayList<>(), "Comedor de prueba", null, "COMEDOR");

    if (id != null) entidad.setId(id);
    return entidad;
  }
}