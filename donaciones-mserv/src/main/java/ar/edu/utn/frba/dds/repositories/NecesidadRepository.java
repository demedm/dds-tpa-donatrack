package ar.edu.utn.frba.dds.repositories;

import java.util.List;
import javax.persistence.EntityManager;
import ar.edu.utn.frba.dds.model.necesidad.Necesidad;
import ar.edu.utn.frba.dds.model.necesidad.Necesidad.TipoNecesidad;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

public class NecesidadRepository implements WithSimplePersistenceUnit {

  private static NecesidadRepository Instance;

  private NecesidadRepository() {}

  public static NecesidadRepository getInstance() {
    if (Instance == null) {
      Instance = new NecesidadRepository();
    }
    return Instance;
  };

  public void guardar(Necesidad necesidad) {
    entityManager().persist(necesidad);
  }

  public Necesidad findById(Long id) {
    return entityManager().find(Necesidad.class, id);
  }

  public List<Necesidad> findAll() {
    return entityManager().createQuery("FROM Necesidad", Necesidad.class).getResultList();
  }

  public List<Necesidad> findAllRecurrentesActivasVencidas() {
    return entityManager().createQuery(
            "FROM Necesidad n WHERE n.tipo = :tipo AND n.estado = :estado AND n.proximoVencimiento < CURRENT_DATE",Necesidad.class)
        .setParameter("tipo", Necesidad.TipoNecesidad.RECURRENTE)
        .setParameter("estado", "en_preparacion")
        .getResultList();
  }

  public List<Necesidad> findAllRecurrentes() {
    return entityManager().createQuery(
            "FROM Necesidad n WHERE n.tipo = :tipo",Necesidad.class)
        .setParameter("tipo", TipoNecesidad.RECURRENTE)
        .getResultList();
  }

  public List<Necesidad> findByEntidadId(String entidadId) {
    List<Necesidad> lista = entityManager()
            .createQuery("FROM Necesidad n WHERE n.entidadId = :entidadId", Necesidad.class)
            .setParameter("entidadId", entidadId)
            .getResultList();

    System.out.println(">>> [DEBUG DB] Cantidad de necesidades encontradas en MariaDB: " + lista.size());
    return lista;
  }
  // public List<Necesidad> findAllVencidas() {
  //   return em.createQuery(
  //       "FROM Necesidad n WHERE n.tipo = :tipo && n.estaVencida()",
  //       Necesidad.class)
  //     .setParameter("tipo", TipoNecesidad.RECURRENTE)
  //     .getResultList();
  // }

  public void eliminar(Long id) {
    Necesidad necesidad = this.findById(id);
    if (necesidad != null) {
      entityManager().remove(necesidad);  // Cascade elimina Peticiones automáticamente
    }
  }

  public Necesidad actualizar(Necesidad necesidad) {
    return entityManager().merge(necesidad);
  }
}
