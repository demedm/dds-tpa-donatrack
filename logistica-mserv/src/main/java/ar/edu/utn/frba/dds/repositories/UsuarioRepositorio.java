package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.model.usuarios.Chofer;
import ar.edu.utn.frba.dds.model.usuarios.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.time.LocalDateTime;
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
  public List<Usuario> mostrarTodos() {
    return entityManager()
        .createQuery("from Usuario")
        .getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<Chofer> mostrarTodosLosChoferes() {
    return entityManager()
        .createQuery("from Chofer")
        .getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<Chofer> mostrarTodasLasEntidadesBeneficiarias() {
    return entityManager()
        .createQuery("from EntidadBeneficiaria ")
        .getResultList();
  }

  public void eliminarUsuario(Usuario usuario) {
    entityManager().remove(usuario);
  }

  @SuppressWarnings("unchecked")
  public Usuario buscarPorId(Long id) {
    return entityManager()
        .createQuery("from Usuario where id = :id", Usuario.class)
        .setParameter("id", id)
        .getResultList().stream().findFirst().orElse(null);
  }

  public Chofer buscarChoferPorId(Long id) {
    return entityManager()
        .createQuery("from Chofer u where u.id = :id", Chofer.class)
        .setParameter("id", id)
        .getResultList().stream().findFirst().orElse(null);
  }

  public EntidadBeneficiaria buscarEntidadBeneficiariaPorId(Long id) {
    return entityManager()
        .createQuery("from EntidadBeneficiaria u where u.id = :id",
            EntidadBeneficiaria.class)
        .setParameter("id", id)
        .getResultList().stream().findFirst().orElse(null);
  }

  public List<Chofer> mostrarChoferesNoAsignados() {
    var rutasConChofer = RutaRepositorio.Instance.mostrarRutasActivasConChoferAsignado();
    var choferesNoDisponibles = rutasConChofer.stream().map(Ruta::getChofer).toList();

    return mostrarTodosLosChoferes().stream().filter(chofer ->
        !choferesNoDisponibles.contains(chofer)).toList();
  }

  /* Entidad Beneficiaria */
  public void noRecepcionaEntrega(Long idEntidad, Long idEntrega) {
    var entrega = EntregaRepositorio.Instance.buscarPorId(idEntrega);
    var entidad = buscarEntidadBeneficiariaPorId(idEntidad);
    if (!entidad.getId().equals(idEntidad)) {
      return; // error (falta excepcion)
    }
    entrega.marcarComoNoRecepcionada();
  }

  public void confirmarEntrega(Long idEntidad, Long idEntrega, Long idCamion) {
    var entrega = EntregaRepositorio.Instance.buscarPorId(idEntrega);
    var camion = CamionRepositorio.Instance.buscarPorId(idCamion);
    if (entrega != null && entrega.getEntidadBeneficiaria().getId().equals(idEntidad)) {
     entrega.marcarComoEntregada(camion, LocalDateTime.now());
    }
  }

}
