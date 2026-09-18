package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.usuarios.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositorio implements WithSimplePersistenceUnit {
  public static final UsuarioRepositorio Instance = new UsuarioRepositorio();
  public List<Usuario> usuarios = new ArrayList<>(); //choferes y admins
  public List<EntidadBeneficiaria> entidades = new ArrayList<>();

  public void registrar(Usuario usuario) {
    entityManager().persist(usuario);
  }

  @SuppressWarnings("unchecked")
  public List<Usuario> getAll() {
    return entityManager()
        .createQuery("from Usuario")
        .getResultList();
  }

  @SuppressWarnings("unchecked")
  public Usuario buscarPorId(Long id) {
    return entityManager()
        .createQuery("from Usuario where id = :id", Usuario.class)
        .setParameter("id", id)
        .getResultList().stream().findFirst().orElse(null);
  }

  public EntidadBeneficiaria buscarEntidadBeneficiariaPorId(Long id) {
    return entityManager()
        .createQuery("from Usuario u where u.id = :id", EntidadBeneficiaria.class)
        .setParameter("id", id)
        .getResultList().stream().findFirst().orElse(null);
  }

  /* Entidad Beneficiaria */
  public void noRecepcionaEntrega(Long idEntidad, Long idEntrega) {
    var entrega = EntregaRepositorio.Instance.buscarPorId(idEntrega);
    var entidad = buscarEntidadBeneficiariaPorId(idEntidad);
    if (!entrega.getEntidadBeneficiaria().getId().equals(idEntidad)) {
      return; // error (falta excepcion)
    }
    entrega.marcarComoNoRecepcionada();
  }

  public void confirmarEntrega(Long idEntidad, Long idEntrega, String urlFoto) {
    var entrega = EntregaRepositorio.Instance.buscarPorId(idEntrega);
    var entidad = buscarEntidadBeneficiariaPorId(idEntidad);
    if (!entrega.getEntidadBeneficiaria().getId().equals(idEntidad)) {
      return; // error (falta excepcion)
    }
    entrega.confirmarEntrega(urlFoto);
  }

}
