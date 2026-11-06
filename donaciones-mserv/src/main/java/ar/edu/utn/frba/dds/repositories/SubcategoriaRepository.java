package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.Bienes.Categoria;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Bienes.UnidadMedida;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

public class SubcategoriaRepository implements WithSimplePersistenceUnit {

  public static final SubcategoriaRepository Instance = new SubcategoriaRepository();

  public Subcategoria buscarOCrear (Categoria categoria, String descripcion, UnidadMedida unidad){
    return entityManager()
        .createQuery("from Subcategoria s where s.categoria = :categoria and s.descripcion = :descripcion", Subcategoria.class)
        .setParameter("categoria",categoria)
        .setParameter("descripcion",descripcion)
        .getResultStream()
        .findFirst()
        .orElseGet(() -> {
          Subcategoria nueva = new Subcategoria(categoria,descripcion,unidad);
          entityManager().persist(nueva);
          return nueva;
        });

  }

}
