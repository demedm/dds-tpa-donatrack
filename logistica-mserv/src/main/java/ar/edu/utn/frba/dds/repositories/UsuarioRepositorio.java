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
import java.util.List;

public class UsuarioRepositorio implements WithSimplePersistenceUnit {
  public static final UsuarioRepositorio Instance = new UsuarioRepositorio();

  public void registrar(Usuario usuario) {
    withTransaction(() -> entityManager().persist(usuario));
  }

  public Usuario buscarXidDonaciones(Long idDonaciones) {
    return entityManager()
        .createQuery(
            "SELECT u FROM Usuario u WHERE u.idDonaciones = :id", Usuario.class
        )
        .setParameter("id", idDonaciones)
        .getResultStream()
        .findFirst()
        .orElse(null);
  }

  public List<Usuario> mostrarTodos() {
    return entityManager()
            .createQuery("from Usuario", Usuario.class)
            .getResultList();
  }

  public List<Chofer> mostrarTodosLosChoferes() {
    return entityManager()
            .createQuery("from Chofer", Chofer.class)
            .getResultList();
  }

  public List<EntidadBeneficiaria> mostrarTodasLasEntidadesBeneficiarias() {
    return entityManager()
            .createQuery("from EntidadBeneficiaria", EntidadBeneficiaria.class)
            .getResultList();
  }

  public void eliminarUsuario(Usuario usuario) {
    withTransaction(() -> entityManager().remove(usuario));
  }

  public Usuario buscarPorId(Long id) {
    return entityManager().find(Usuario.class, id);
  }

  public Chofer buscarChoferPorId(Long id) {
    return entityManager().find(Chofer.class, id);
  }

  public EntidadBeneficiaria buscarEntidadBeneficiariaPorId(Long id) {
    EntidadBeneficiaria beneficiaria = entityManager().find(EntidadBeneficiaria.class, id);
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

    return choferes.stream()
            .filter(chofer -> !choferesNoDisponibles.contains(chofer))
            .toList();
  }

  /* Entidad Beneficiaria */
  public Entrega noRecepcionaEntrega(EntidadBeneficiaria entidadBeneficiaria, Long idEntrega) {
    return withTransaction(() -> {
      var entrega = EntregaRepositorio.Instance.buscarPorId(idEntrega);
      if (entrega != null
              && entrega.getEntidadBeneficiaria() != null
              && entrega.getEntidadBeneficiaria().getId().equals(entidadBeneficiaria.getId())
              && !entrega.estaVencida()) {
        entrega.marcarComoNoRecepcionada();
      }
      return entrega;
    });
  }

  public Entrega confirmarEntrega(EntidadBeneficiaria entidadBeneficiaria,
                                  Long idEntrega,
                                  ConfirmacionEntregaDto dto) {
    return withTransaction(() -> {
      var entrega = EntregaRepositorio.Instance.buscarPorId(idEntrega);
      var camion = CamionRepositorio.Instance.buscarPorId(dto.getCamionId());
      if (entrega != null
              && entrega.getEntidadBeneficiaria() != null
              && entrega.getEntidadBeneficiaria().getId().equals(entidadBeneficiaria.getId())
              && !entrega.estaVencida()) {
        entrega.marcarComoEntregada(camion, LocalDateTime.of(
                dto.getAnio(), dto.getMes(), dto.getDia(), dto.getHora(), dto.getMinutos()));
      }
      return entrega;
    });
  }

  public Usuario buscarXmail(String email) {
    var usuario = entityManager().createQuery(
            "from Usuario u where u.email = :email", Usuario.class)
        .setParameter("email", email)
        .getResultList().stream().findFirst().orElse(null);
    if (usuario == null) {
      throw new UsuarioNotFoundException(email);
    }
    return usuario;
  }
}
