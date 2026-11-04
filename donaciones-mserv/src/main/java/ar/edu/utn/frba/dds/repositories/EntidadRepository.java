package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;

public class EntidadRepository implements WithSimplePersistenceUnit {
  public static EntidadRepository Instance = new EntidadRepository();


  public void registrar(EntidadBeneficiaria entidad) {
    withTransaction(() -> {
        entityManager().persist(entidad);
        entidad.getNecesidades().forEach(necesidad ->
            necesidad.setEntidadId(entidad.getId()));
    });
  }

  public EntidadBeneficiaria obtenerPorId(Long id) {
    return entityManager().find(EntidadBeneficiaria.class, id);
  }

  public List<EntidadBeneficiaria> obtenerEntidades() {
    return entityManager()
        .createQuery("from EntidadBeneficiaria ",
            EntidadBeneficiaria.class)
        .getResultList();
  }

  public EntidadBeneficiaria actualizarEntidad(EntidadBeneficiaria entidadExistente, EntidadBeneficiaria camposActualizar) {
    withTransaction(() -> {

      entidadExistente.setDireccion(camposActualizar.getDireccion());
      entidadExistente.setTelefono(camposActualizar.getTelefono());
      entidadExistente.setMailsContacto(camposActualizar.getMailsContacto());

    });
    return entidadExistente;
  }

  public boolean eliminarPorId(Long id) {
    EntidadBeneficiaria entidad = obtenerPorId(id);
    if (entidad == null) {
      return false;
    }

    withTransaction(() -> entityManager().remove(entidad));
    return true;
  }
}
