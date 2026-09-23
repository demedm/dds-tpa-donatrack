package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.EstadoCamion;
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
    entityManager().persist(camion);
  }

  public void eliminarCamion(Camion camion) {
    entityManager().remove(camion);
  }

  @SuppressWarnings("unchecked")
  public List<Camion> mostrarTodos() {
    return entityManager()
        .createQuery("from Camion")
        .getResultList();
  }

  public Camion buscarPorId(Long id) {
    return entityManager()
        .createQuery("from Camion c where c.id = :id", Camion.class)
        .setParameter("id", id)
        .getResultList().stream().findFirst().orElse(null);
  }

  public Camion buscarPorPatente(String patente) {
    return entityManager()
        .createQuery("from Camion c where c.patente = :patente", Camion.class)
        .setParameter("patente", patente)
        .getResultList().stream().findFirst().orElse(null);
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

}