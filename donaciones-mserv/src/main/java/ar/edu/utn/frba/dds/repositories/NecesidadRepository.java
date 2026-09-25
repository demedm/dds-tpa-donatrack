package ar.edu.utn.frba.dds.repositories;

import java.util.List;
import javax.persistence.EntityManager;
import ar.edu.utn.frba.dds.model.necesidad.Necesidad;
import ar.edu.utn.frba.dds.model.necesidad.Necesidad.TipoNecesidad;

public class NecesidadRepository {

  private static NecesidadRepository Instance; // Uso lazy init para ue lo haga cuando lo pida

  private EntityManager em;

  private NecesidadRepository() {
      this.em = EntityManagerHelper.getEntityManager();
    }

  public static NecesidadRepository getInstance() {
      if (Instance == null) {
        Instance = new NecesidadRepository();
      }
      return Instance;
    };

  public void guardar(Necesidad necesidad) {
        em.getTransaction().begin();
        em.persist(necesidad);
        em.getTransaction().commit();
    }

  public Necesidad findById(Long id) {
        return em.find(Necesidad.class, id);
    }

    public List<Necesidad> findAll() {
        return em.createQuery("FROM Necesidad", Necesidad.class).getResultList();
    }

  public List<Necesidad> findAllRecurrentesActivasVencidas() {
    return em.createQuery(
        "FROM Necesidad n WHERE n.tipo = :tipo AND n.estado = :estado AND n.proximoVencimiento < CURRENT_DATE",Necesidad.class)
      .setParameter("tipo", Necesidad.TipoNecesidad.RECURRENTE)
      .setParameter("estado", "en_preparacion")
      .getResultList();
}
  
  public List<Necesidad> findAllRecurrentes() {
    return em.createQuery(
        "FROM Necesidad n WHERE n.tipo = :tipo",Necesidad.class)
      .setParameter("tipo", TipoNecesidad.RECURRENTE)
      .getResultList();
}
  // public List<Necesidad> findAllVencidas() {
  //   return em.createQuery(
  //       "FROM Necesidad n WHERE n.tipo = :tipo && n.estaVencida()", 
  //       Necesidad.class)
  //     .setParameter("tipo", TipoNecesidad.RECURRENTE)
  //     .getResultList();
  // }

  public void eliminar(Long id) {
    em.getTransaction().begin();
    Necesidad necesidad = em.find(Necesidad.class, id);
    if (necesidad != null) {
      em.remove(necesidad);  // Cascade elimina Peticiones automáticamente
    }
    em.getTransaction().commit();
  }

  // actualizo el que esta en el repositorio

  public Necesidad actualizar(Necesidad necesidad) {
    em.getTransaction().begin();
    Necesidad actualizada = em.merge(necesidad);  // merge en vez de persist, cambia lo necesario. 
    em.getTransaction().commit();
    return actualizada;
  }
}
