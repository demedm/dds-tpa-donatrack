package ar.edu.utn.frba.dds.Persistencia;

import ar.edu.utn.frba.dds.model.Asignacion.AlgoritmoAsignacion;
import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.BienDuradero;
import ar.edu.utn.frba.dds.model.Bienes.BienPerecedero;
import ar.edu.utn.frba.dds.model.Bienes.Categoria;
import ar.edu.utn.frba.dds.model.Bienes.EstadoUso;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;
import ar.edu.utn.frba.dds.repositories.EntidadRepository;
import ar.edu.utn.frba.dds.tareas.ProcesarDonaciones;
import com.mchange.util.AssertException;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
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

  private Subcategoria arroz;
  private Subcategoria silla;

  @BeforeEach
  void setUp() {
    arroz = new Subcategoria(Categoria.ALIMENTOS, "ARROZ");
    silla = new Subcategoria(Categoria.MOBILIARIO, "SILLA");

  }

  @AfterEach
  void borrarLoCreado(){
    donacionesCreadas.forEach(repo::eliminar);
  }

  @Test
  void seGuardaLaDonacionConSusSegementadas(){
    Donacion donacion = donacionDeArrozYSilla();

    Donacion leida = guardarYReleer(donacion);

    assertNotNull(leida);
    assertEquals("Donacino de prueba", leida.getDescripcionGeneral());
    assertEquals(2,leida.getDonaciones().size());

  }

  @Test
  void lasSegmentadasConservanSuCantidad(){
    Donacion leida = guardarYReleer(donacionDeArrozYSilla());

    assertEquals(arroz, segmentadaDe(leida,arroz).getSubcategoria());

  }

  @Test
  void eliminarLaDonacionBorrarTambienSusSegmentadas(){
    Donacion donacion = donacionDeArrozYSilla();
    Long idSegmentada = donacion.getDonaciones().get(0).getId();
    repo.guardar(donacion);

    repo.eliminar(donacion.getId());
    entityManager().clear();

    assertNull(repo.findById(donacion.getId()));
    assertNull(repo.findById(idSegmentada));

  }

  //bienes
  @Test
  void unBienPerecederoVuelveComoPerecedero() {
    Donacion leida = guardarYReleer(donacionDeArrozYSilla());

    assertInstanceOf(BienPerecedero.class, segmentadaDe(leida,arroz).getBienFiltrado());
  }

  @Test
  void unBienDuraderoVuelveComoDuraderoConSuEstado() {
    Donacion leida = guardarYReleer(donacionDeArrozYSilla());

    Bien bien =segmentadaDe(leida,silla).getBienFiltrado();

    assertInstanceOf(BienDuradero.class, bien);
    assertEquals(EstadoUso.NUEVO,bien.getCriterioDeAgrupacion().criterio());
  }

  //matchmaking

  @Test
  void lasPropuestasDelMatchmakingQuedanGuardadas(){

    Donacion donacion = donacionDeArrozYSilla();
    Long idSegmentada = segmentadaDe(donacion,arroz).getId();
    repo.guardar(donacion);

    EntidadBeneficiaria comedor = entidad("ENT-TEST-1");
    AlgoritmoAsignacion algoritmo = mock(AlgoritmoAsignacion.class);
    when(algoritmo.obtenerRanking(any(),any())).thenReturn(List.of(comedor));

    withTransaction(() ->
        repo.findSegmentadaById(idSegmentada).buscarCandidatas(List.of(comedor), List.of(algoritmo)));
    entityManager().clear();

    assertEquals(List.of("ENT-TEST-1"),repo.findSegmentadaById(idSegmentada).getIdsEntidadesPropuestas());

  }
  @Test
  void elCronDejaLasPropuestasEnLaBase(){

    Donacion donacion = donacionDeArrozYSilla();
    Long idSegmentada = segmentadaDe(donacion,arroz).getId();
    repo.guardar(donacion);

    EntidadBeneficiaria comedor = entidad(null);
    EntidadRepository.Instance.registrar(comedor);

    ProcesarDonaciones.ejecutar();
    entityManager().clear();

    assertTrue(repo.findSegmentadaById(idSegmentada).getIdsEntidadesPropuestas().contains(comedor.getId()));

  }
  @Test
  @Disabled
  void soloTraeLasSegmentadasQueSiSiguenEnDeposito(){
    Donacion donacion = donacionDeArrozYSilla();
    DonacionSegmentada asignada = segmentadaDe(donacion,silla);

    asignada.asignar();
    repo.guardar(donacion);
    entityManager().clear();

    List<Long> enDeposito = repo.findSegmentadasEnDeposito().stream()
        .map(DonacionSegmentada::getId).toList();

    assertTrue(enDeposito.contains(segmentadaDe(donacion,arroz).getId()));
    assertFalse(enDeposito.contains(asignada.getId()));

  }



  private Donacion donacionDeArrozYSilla(){
    Date vencimiento = new Date();
    List<Bien> bienes = new ArrayList<>(List.of(
        new BienPerecedero(arroz, "arroz.jpg","Arroz 1kg",vencimiento),
        new BienPerecedero(arroz, "arroz.jp","Arroz 1kg", vencimiento),
        new BienDuradero(silla,"silla.jpg","Silla de madera", EstadoUso.NUEVO)));

    Donacion donacion = new Donacion("Donacino de prueba", bienes, null);
    donacionesCreadas.add(donacion.getId());
    return donacion;

  }

  private Donacion guardarYReleer(Donacion donacion){
    repo.guardar(donacion);
    entityManager().clear();
    return repo.findById(donacion.getId());
  }

  private static DonacionSegmentada segmentadaDe(Donacion donacion, Subcategoria subcategoria){
    return donacion.getDonaciones().stream()
        .filter(s -> s.getSubcategoria().equals(subcategoria))
        .findFirst()
        .orElseThrow(() -> new AssertException("No hay segmentada de " + subcategoria.getDescripcion()));
  }

  private static EntidadBeneficiaria entidad(String id){
    EntidadBeneficiaria entidad =
        new EntidadBeneficiaria("Calle falsa 123", new ArrayList<>(),"Comedor de prueba", null, "COMEDOR");

    if(id != null) entidad.setId(id);
    return entidad;
  }


}
