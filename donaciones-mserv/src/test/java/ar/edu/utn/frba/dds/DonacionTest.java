package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.BienDuradero;
import ar.edu.utn.frba.dds.model.Bienes.BienPerecedero;
import ar.edu.utn.frba.dds.model.Bienes.Categoria;
import ar.edu.utn.frba.dds.model.Bienes.EstadoUso;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DonacionTest {

  private Categoria categoria;
  private Subcategoria subFideos;
  private Subcategoria subSillas;
  private DonacionesRepository repository;

@BeforeEach
  public void setUp() {

    categoria = new Categoria();
    subFideos = new Subcategoria(categoria, "fideos");
    subSillas = new Subcategoria(categoria, "sillas");
    repository = new DonacionesRepository();

}

@Test
  public void unaListaDeBienesDistintosGeneraVariasSegmentadas() {

    List<Bien> bienes = List.of(
        new BienPerecedero(subFideos,null,"fideos",new Date()),
        new BienDuradero(subSillas,null,"silla", EstadoUso.USADO)
    );

    Donacion donacion = new Donacion("desc",bienes,null);

    assertEquals(2,donacion.getDonaciones().size());

  }


}
