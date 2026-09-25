package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.exceptions.CamionNotFoundException;
import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.EstadoCamion;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.model.Ubicacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class CamionRepositorio implements WithSimplePersistenceUnit {
  public static final CamionRepositorio Instance = new CamionRepositorio();

  public void reportarImprevisto(Long id) {
    Camion camion = buscarPorId(id);
    camion.improvistoLogistico();
    var rutaEnCurso = RutaRepositorio.Instance.buscarRutaEnCursoDeCamion(id);
    if (rutaEnCurso != null) {
      rutaEnCurso.indicarImprovistoLogistico();
      rutaEnCurso.getEntregas().forEach(EntregaRepositorio.Instance::notificarFalloDeEntrega);
    }
  }

  public void registrar(Camion camion) {
    entityManager().getTransaction().begin();
    entityManager().persist(camion);
    entityManager().getTransaction().commit();
  }

  public void eliminarCamion(Camion camion) {
    entityManager().getTransaction().begin();
    entityManager().remove(camion);
    entityManager().getTransaction().commit();
  }

  public Camion actualizar(Camion camion) {
    entityManager().getTransaction().begin();
    Camion actualizado = entityManager().merge(camion);
    entityManager().getTransaction().commit();
    return actualizado;
  }

  @SuppressWarnings("unchecked")
  public List<Camion> mostrarTodos() {
    return entityManager()
        .createQuery("from Camion")
        .getResultList();
  }

  public Camion buscarPorId(Long id) {
    var camion = entityManager()
        .createQuery("from Camion c where c.id = :id", Camion.class)
        .setParameter("id", id)
        .getResultList().stream().findFirst().orElse(null);
    if (camion == null) {
      throw new CamionNotFoundException(id);
    }
    return camion;
  }

  public Camion buscarPorPatente(String patente) {
    var camion = entityManager()
        .createQuery("from Camion c where c.patente = :patente", Camion.class)
        .setParameter("patente", patente)
        .getResultList().stream().findFirst().orElse(null);
    if (camion == null) {
      throw new CamionNotFoundException(patente);
    }
    return camion;
  }

  @SuppressWarnings("unchecked")
  public List<Camion> mostrarCamionesDisponibles() {
    return entityManager()
        .createQuery("from Camion c where c.estado = :estado")
        .setParameter("estado", EstadoCamion.DISPONIBLE)
        .getResultList();
  }

  public Ubicacion verUbicacionDeCamion(Camion camion) {
    return entityManager()
        .createQuery("from Ubicacion u where u.id = :id", Ubicacion.class)
        .setParameter("id", camion.getId())
        .getResultList().stream().findFirst().orElse(null);
  }

  public EstadoCamion buscarEstadoPorId(Long id) {
    return entityManager()
        .createQuery("select c.estado from Camion c where c.id = :id", EstadoCamion.class)
        .setParameter("id", id)
        .getSingleResult();
  }
}