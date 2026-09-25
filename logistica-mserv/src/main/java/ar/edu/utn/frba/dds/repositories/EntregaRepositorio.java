package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.exceptions.EntregaNotFoundException;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.EstadoCamion;
import ar.edu.utn.frba.dds.model.EstadoEntrega;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.model.accionesentregas.AccionesSobreEntregas;
import ar.edu.utn.frba.dds.model.accionesentregas.Notificar;
import ar.edu.utn.frba.dds.model.accionesentregas.NotificarAdmins;
import ar.edu.utn.frba.dds.scripts.dto.DestinoDto;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;

public class EntregaRepositorio implements WithSimplePersistenceUnit {
  public static final EntregaRepositorio Instance = new EntregaRepositorio();
  private final List<AccionesSobreEntregas> observers = new ArrayList<>();

  public EntregaRepositorio() {
    observers.add(new NotificarAdmins());
    observers.add(new Notificar());
  }

  public void notificarInicioDeEntrega(Entrega entrega) {
    observers.forEach(o -> o.notificarInicioRuta(entrega));
  }

  public void notificarFalloDeEntrega(Entrega entrega) {
    observers.forEach(o -> o.notificarFalloEntrega(entrega));
  }

  public Entrega crearEntrega(DestinoDto dto) {
    return new Entrega(dto.getDireccion(), dto.getDonacionId());
  }

  private boolean iniciarTransaccion() {
    if (!entityManager().getTransaction().isActive()) {
      entityManager().getTransaction().begin();
      return true;
    }
    return false;
  }

  private void commit(boolean transaccionPropia) {
    if (transaccionPropia) {
      entityManager().getTransaction().commit();
    }
  }

  public void registrar(Entrega entrega) {
    EntityTransaction transaction = entityManager().getTransaction();
    boolean transaccionPropia = iniciarTransaccion();

    entityManager().persist(entrega);

    commit(transaccionPropia);
  }

  public void eliminarEntrega(Entrega entrega) {
    EntityTransaction transaction = entityManager().getTransaction();
    boolean transaccionPropia = iniciarTransaccion();

    entityManager().remove(entrega);

    commit(transaccionPropia);
  }

  public Entrega actualizar(Entrega entrega) {
    entityManager().getTransaction().begin();
    Entrega actualizada = entityManager().merge(entrega);
    entityManager().getTransaction().commit();
    return actualizada;
  }

  @SuppressWarnings("unchecked")
  public List<Entrega> mostrarTodos() {
    return entityManager()
        .createQuery("from Entrega")
        .getResultList();
  }

  public Entrega buscarPorId(Long id) {
    var entrega = entityManager()
        .createQuery("from Entrega e where e.id = :id", Entrega.class)
        .setParameter("id", id)
        .getResultList().stream().findFirst().orElse(null);
    if (entrega == null) {
      throw new EntregaNotFoundException(id);
    }
    return entrega;
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

  @SuppressWarnings("unchecked")
  public List<Entrega> buscarEntregasDeRuta(Long idRuta) {
    return entityManager()
        .createQuery("select r.entregas from Ruta r where r.id = :id")
        .setParameter("id", idRuta)
        .getResultList();
  }

  public EstadoEntrega buscarEstadoPorId(Long id) {
    return entityManager()
        .createQuery("select e.estado from Entrega e where e.id = :id", EstadoEntrega.class)
        .setParameter("id", id)
        .getSingleResult();
  }

}
