package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.exceptions.CamionNotFoundException;
import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.EstadoCamion;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.model.Ubicacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import java.util.List;

public class CamionRepositorio implements WithSimplePersistenceUnit {
  public static final CamionRepositorio Instance = new CamionRepositorio();

  public Ruta reportarImprevisto(Long id) {
    return withTransaction(() -> {
      Camion camion = buscarPorId(id);
      camion.improvistoLogistico();
      Ruta ruta = RutaRepositorio.Instance.buscarRutaEnCursoDeCamion(id);
      if (ruta != null) {
        ruta.indicarImprevistoLogistico();
      }
      return ruta;
    });
  }

  public void registrar(Camion camion) {
    withTransaction(()->entityManager().persist(camion));
  }

  public void eliminarCamion(Camion camion) {
    withTransaction(()->entityManager().remove(camion));
  }

  public Camion actualizar(Camion camion) {
    return withTransaction(()->entityManager().merge(camion));
  }

  @SuppressWarnings("unchecked")
  public List<Camion> mostrarTodos() {
    return entityManager()
        .createQuery("from Camion",Camion.class)
        .getResultList();
  }

  public Camion buscarPorId(Long id) {
    Camion camion = entityManager().find(Camion.class,id);
    if (camion==null){
      throw new CamionNotFoundException(id);
    }
    return camion;
  }

  public Camion buscarPorPatente(String patente) {
    List <Camion> resultados = entityManager()
            .createQuery("from Camion c where c.patente = :patente",Camion.class)
            .setParameter("patente",patente)
            .getResultList();
    if (resultados.isEmpty()){
      throw new CamionNotFoundException(patente);
    }
    return resultados.get(0);
  }

  @SuppressWarnings("unchecked")
  public List<Camion> mostrarCamionesDisponibles() {
    return entityManager()
            .createQuery("from Camion c where c.estado = :estado", Camion.class)
            .setParameter("estado", EstadoCamion.DISPONIBLE)
            .getResultList();
  }

  public Ubicacion verUbicacionDeCamion(Camion camion) {
    List<Ubicacion> resultados = entityManager()
            .createQuery("from Ubicacion u where u.camion.id = :id", Ubicacion.class)
            .setParameter("id", camion.getId())
            .getResultList();

    return resultados.isEmpty() ? null : resultados.get(0);
  }

  public EstadoCamion buscarEstadoPorId(Long id) {
    return entityManager()
            .createQuery("select c.estado from Camion c where c.id = :id", EstadoCamion.class)
            .setParameter("id", id)
            .getSingleResult();
  }

  public EstadoCamion buscarPatentePorId(Long id) {
    return entityManager()
        .createQuery("select c.patente from Camion c where c.id = :id", EstadoCamion.class)
        .setParameter("id", id)
        .getSingleResult();
  }
}