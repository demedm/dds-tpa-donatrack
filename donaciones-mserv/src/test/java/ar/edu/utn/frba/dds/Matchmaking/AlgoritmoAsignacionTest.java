/*package ar.edu.utn.frba.dds.Matchmaking;

import ar.edu.utn.frba.dds.model.Asignacion.AlgoritmoDeCompatibilidad;
import ar.edu.utn.frba.dds.model.Asignacion.AlgoritmoSubatendidos;
import ar.edu.utn.frba.dds.model.Bienes.Categoria;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.necesidad.Necesidad;
import ar.edu.utn.frba.dds.model.necesidad.Peticion;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AlgoritmoAsignacionTest {

  private final DonacionSegmentada donacionDeArroz =
      new DonacionSegmentada(10, new Subcategoria(Categoria.ALIMENTOS, "ARROZ"),null);

  //Algoritmo

  @Nested
  class AlgoritmoDeCompatibilidadTest {
    private final AlgoritmoDeCompatibilidad algoritmo = new AlgoritmoDeCompatibilidad();

    @Test
    void primeroVanLasEntidadesMasNecesitanLoDonado(){
      EntidadBeneficiaria sinNecesidades = entidadQueNecesita("Sin necesidades");
      EntidadBeneficiaria unaPeticion = entidadQueNecesita("una peticion","ARROZ");
      EntidadBeneficiaria dosPeticiones = entidadQueNecesita("dos peticiones", "ARROZ","ARROZ");

      List<EntidadBeneficiaria> ranking = algoritmo.obtenerRanking(
          donacionDeArroz, List.of(sinNecesidades,unaPeticion,dosPeticiones));

      assertEquals(List.of(dosPeticiones,unaPeticion,sinNecesidades),ranking);

    }

    @Test
    void necesitarOtraCosaNoSumaCompatibilidad(){
      EntidadBeneficiaria quiereFideos = entidadQueNecesita("Quiere fideos", "FIDEOS");
      EntidadBeneficiaria quiereArroz = entidadQueNecesita("Quiere arroz", "ARROZ");

      List<EntidadBeneficiaria> ranking = algoritmo.obtenerRanking(
        donacionDeArroz, List.of(quiereFideos,quiereArroz)
      );

      assertEquals(quiereArroz,ranking.get(0));
    }

    @Test
    void proponeComoMaximoDiezEntidades(){
      List<EntidadBeneficiaria> doce = IntStream.range(0,12)
          .mapToObj(i -> entidadQueNecesita("Entidad " + i)).toList();

      assertEquals(10, algoritmo.obtenerRanking(donacionDeArroz, doce).size());
    }
    @Test
    void sinEntidadesDevuelveUnRankingVacio() {
      assertTrue(algoritmo.obtenerRanking(donacionDeArroz, List.of()).isEmpty());
    }

  }

  @Nested
  class Subatendidos {
    private final AlgoritmoSubatendidos algoritmo = new AlgoritmoSubatendidos();

    @Test
    void primeroVanLasQueRecibieronMenosDonaciones() {
      EntidadBeneficiaria recibioTres = entidadQueRecibio("Recibio 3", 3, LocalDate.now().minusDays(10));
      EntidadBeneficiaria recibioUna = entidadQueRecibio("Recibio 1", 1, LocalDate.now().minusDays(10));
      EntidadBeneficiaria noRecibio = entidadQueRecibio("No recibio", 0, null);

      List<EntidadBeneficiaria> ranking = algoritmo.obtenerRanking(
          donacionDeArroz, List.of(recibioTres, recibioUna, noRecibio));

      assertEquals(List.of(noRecibio, recibioUna, recibioTres), ranking);
    }

    @Test
    void cuentaLasDonacionesDelUltimoTrimestre() {
      EntidadBeneficiaria recibioHaceUnMes = entidadQueRecibio("Hace 1 mes", 2, LocalDate.now().minusMonths(1));

      assertEquals(2, algoritmo.cantidadDonaciones(recibioHaceUnMes));
    }

    @Test
    void lasDonacionesDeHaceMasDeTresMesesNoCuentan() {
      EntidadBeneficiaria recibioHaceMucho = entidadQueRecibio("Hace 6 meses", 5, LocalDate.now().minusMonths(6));

      assertEquals(0, algoritmo.cantidadDonaciones(recibioHaceMucho));
    }

    @Test
    void proponeComoMaximoDiezEntidades() {
      List<EntidadBeneficiaria> doce = IntStream.range(0, 12)
          .mapToObj(i -> entidadQueRecibio("Entidad " + i, 0, null)).toList();

      assertEquals(10, algoritmo.obtenerRanking(donacionDeArroz, doce).size());
    }
  }



  //ayuda

  private static EntidadBeneficiaria entidad(String nombre){
    return new EntidadBeneficiaria("Calle falsa 123", new ArrayList<>(), nombre, null, "COMEDOR");
  }

  private static EntidadBeneficiaria entidadQueNecesita(String nombre, String... subcategorias){
    EntidadBeneficiaria entidad = entidad(nombre);
    Necesidad necesidad = new Necesidad();

    for (String subcategoria : subcategorias) {
      necesidad.agregarPeticion(new Peticion(subcategoria,1));
    }
    entidad.getNecesidades().add(necesidad);
    return entidad;
  }

  private static EntidadBeneficiaria entidadQueRecibio(String nombre, int cantidad, LocalDate fecha){
    EntidadBeneficiaria entidad = entidad(nombre);

    for (int i =0; i < cantidad; i++){
      DonacionSegmentada recibida = mock(DonacionSegmentada.class);
      when(recibida.getFechaDeEntrega()).thenReturn(fecha);
      entidad.getDonacionesRecibidas().add(recibida);
    }
    return entidad;
  }

}

 */