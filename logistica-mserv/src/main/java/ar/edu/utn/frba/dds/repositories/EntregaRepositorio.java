package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.EstadoEntrega;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class EntregaRepositorio implements WithSimplePersistenceUnit {
  public static final EntregaRepositorio Instance = new EntregaRepositorio();

  public void registrar(Entrega entrega) {
    entityManager().persist(entrega);
  }

  @SuppressWarnings("unchecked")
  public List<Entrega> mostrarTodos() {
    return entityManager()
        .createQuery("from Entrega")
        .getResultList();
  }

  public Entrega buscarPorId(Long id) {
    return entityManager()
        .createQuery("from Entrega e where e.id = :id", Entrega.class)
        .setParameter("id", id)
        .getSingleResult();
  }

  @SuppressWarnings("unchecked")
  public List<Entrega> buscarEntregasDeRuta(Long idRuta) {
    return entityManager()
        .createQuery("from Entrega e where e.ruta.id = :id")
        .setParameter("id", idRuta)
        .getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<Entrega> buscarEntregasDeEntidadBeneficiaria(Long idEntidad) {
    return entityManager()
        .createQuery("from Entrega e where e.entidadBeneficiaria.id = :id")
        .setParameter("id", idEntidad)
        .getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<Entrega> buscarEntregasConEstado(EstadoEntrega estado) {
    return entityManager()
        .createQuery("from Entrega e where e.estado = :estado")
        .setParameter("estado", estado)
        .getResultList();
  }

}
