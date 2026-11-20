package ar.edu.utn.frba.dds.repositories.monitoreo;

import ar.edu.utn.frba.dds.exceptions.CamionNotFoundException;
import ar.edu.utn.frba.dds.exceptions.DepositoNotFoundException;
import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.monitoreo.Deposito;
import ar.edu.utn.frba.dds.repositories.CamionRepositorio;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;

public class MonitoreoRepositorio implements WithSimplePersistenceUnit {
  public static final MonitoreoRepositorio Instance = new MonitoreoRepositorio();

  public void registrar(Deposito deposito) {
    withTransaction(()->entityManager().persist(deposito));
  }

  public void eliminar(Deposito deposito) {
    withTransaction(()->entityManager().remove(deposito));
  }

  public Deposito buscarXid(Long id) {
    Deposito deposito = entityManager().find(Deposito.class,id);
    if (deposito == null){
      throw new DepositoNotFoundException(id);
    }
    return deposito;
  }

  public List<Deposito> mostrarTodosDepositos() {
    return entityManager()
        .createQuery("from Deposito", Deposito.class)
        .getResultList();
  }

  public Deposito actualizar(Deposito deposito) {
    return withTransaction(()->entityManager().merge(deposito));
  }

  public Deposito guardar(Deposito deposito) {
    return withTransaction(() -> {
      Deposito guardado = entityManager().merge(deposito); // sirve para alta y para modificación
      entityManager().flush(); // asegura que el id esté generado

      if (guardado.isPrincipal()) {
        entityManager()
            .createQuery("update Deposito d set d.esPrincipal = false "
                + "where d.esPrincipal = true and d.id <> :id")
            .setParameter("id", guardado.getId())
            .executeUpdate();
      }
      return guardado;
    });
  }

  public Deposito buscarPrincipal() {
    return entityManager()
        .createQuery("from Deposito d where d.esPrincipal = true", Deposito.class)
        .getResultStream()
        .findFirst()
        .orElse(null);
  }

}
