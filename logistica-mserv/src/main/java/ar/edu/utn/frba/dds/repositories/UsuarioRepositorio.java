package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.exceptions.UsuarioNotFoundException;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.model.usuarios.Chofer;
import ar.edu.utn.frba.dds.model.usuarios.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.scripts.dto.ConfirmacionEntregaDto;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositorio implements WithSimplePersistenceUnit {
  public static final UsuarioRepositorio Instance = new UsuarioRepositorio();
  public List<Usuario> usuarios = new ArrayList<>(); //choferes y admins
  public List<EntidadBeneficiaria> entidades = new ArrayList<>();

  public void registrar(Usuario usuario) {
    entityManager().getTransaction().begin();
    entityManager().persist(usuario);
    entityManager().getTransaction().commit();
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
    entityManager().getTransaction().begin();
    entityManager().remove(usuario);
    entityManager().getTransaction().commit();
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
    var beneficiaria = entityManager()
        .createQuery("from EntidadBeneficiaria u where u.id = :id",
            EntidadBeneficiaria.class)
        .setParameter("id", id)
        .getResultList().stream().findFirst().orElse(null);
    if (beneficiaria == null) {
      throw new UsuarioNotFoundException(id);
    }
    return beneficiaria;
  }



  public List<Chofer> mostrarChoferesNoAsignados() {
    var choferes = mostrarTodosLosChoferes();
    var rutasConChofer = RutaRepositorio.Instance.mostrarRutasActivasConChoferAsignado();

    if (rutasConChofer.isEmpty()) {
      return choferes;
    }

    List<Chofer> choferesNoDisponibles = rutasConChofer.stream().map(Ruta::getChofer).toList();

    return choferes.stream().filter(chofer ->
        !choferesNoDisponibles.contains(chofer)).toList();
  }

  /* Entidad Beneficiaria */
  public void noRecepcionaEntrega(EntidadBeneficiaria entidadBeneficiaria, Long idEntrega) {
    entityManager().getTransaction().begin();
    var entrega = EntregaRepositorio.Instance.buscarPorId(idEntrega);
    if (entrega.getEntidadBeneficiaria().getId().equals(entidadBeneficiaria.getId())
      && !entrega.estaVencida()) {
      entrega.marcarComoNoRecepcionada();
      // EntregaRepositorio.Instance.notificarFalloDeEntrega(entrega);
    }
    entityManager().getTransaction().commit();
  }

  public void confirmarEntrega(EntidadBeneficiaria entidadBeneficiaria,
                               Long idEntrega,
                               ConfirmacionEntregaDto dto) {
    entityManager().getTransaction().begin();
    var entrega = EntregaRepositorio.Instance.buscarPorId(idEntrega);
    var camion = CamionRepositorio.Instance.buscarPorId(dto.getCamionId());
    if (entrega.getEntidadBeneficiaria().getId().equals(entidadBeneficiaria.getId())
      && !entrega.estaVencida()) {
      entrega.marcarComoEntregada(camion, LocalDateTime.of(
          dto.getAnio(), dto.getMes(), dto.getDia(), dto.getHora(), dto.getMinutos()));
    }
    entityManager().getTransaction().commit();
  }

}
