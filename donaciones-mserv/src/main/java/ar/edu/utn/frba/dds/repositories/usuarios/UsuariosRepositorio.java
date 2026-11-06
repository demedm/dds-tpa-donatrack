package ar.edu.utn.frba.dds.repositories.usuarios;

import ar.edu.utn.frba.dds.GrpcClientLogistica.GrpcClientLogisticaImplementation;
import ar.edu.utn.frba.dds.dto.autenticacion.RegistroRequest;
import ar.edu.utn.frba.dds.exceptions.ErrorLogisticaException;
import ar.edu.utn.frba.dds.exceptions.UsuarioNotFoundException;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.DonanteRepository;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;

public class UsuariosRepositorio implements WithSimplePersistenceUnit {
  public static final UsuariosRepositorio Instance = new UsuariosRepositorio();

  public UsuariosRepositorio() {

  }

  // si el donante no esta registrado, lo registra
  public void registrarUsuarioDeDonante(Usuario usuario, Persona donante) {
    EntityManager em = entityManager();

    try {
      Persona existente = DonanteRepository.Instance.buscarXmail(donante
          .getMail().getMedioContacto());
      em.getTransaction().begin();
      if (existente == null) {
        em.persist(donante);
        em.flush();

        usuario.setDominioId(donante.getId());
        em.persist(usuario);
        em.getTransaction().commit();
        return;
      }

      usuario.setDominioId(existente.getId());
      em.persist(usuario);
      em.getTransaction().commit();
    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw e;
    }
  }

  public void eliminar(Usuario usuario) {
    entityManager().getTransaction().begin();
    entityManager().remove(usuario);
    entityManager().getTransaction().commit();
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

  public void registrarUsuarioDeBeneficiario(
      Usuario usuario,
      EntidadBeneficiaria beneficiaria,
      RegistroRequest registro
  ) {
    EntityManager em = entityManager();

    try {
      em.getTransaction().begin();
      em.persist(beneficiaria);
      em.flush();

      usuario.setDominioId(beneficiaria.getId());
      em.persist(usuario);
      em.flush();

      boolean enviado = new GrpcClientLogisticaImplementation()
          .enviarBeneficiario(usuario, registro);

      if (!enviado) {
        throw new ErrorLogisticaException(
            "Logística no pudo registrar el beneficiario."
        );
      }

      em.getTransaction().commit();

    } catch (ErrorLogisticaException e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw e;

    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new RuntimeException(
          "No se pudo registrar el beneficiario.", e
      );
    }
  }

  public void registrarUsuarioDeAdmin(Usuario usuario) {
    EntityManager em = entityManager();

    try {
      em.getTransaction().begin();

      em.persist(usuario);
      em.flush();

      // el administrador no tiene una entidad de dominio propia.
      // tiene su propio id como dominioId.
      usuario.setDominioId(usuario.getId());
      em.flush();

      boolean enviado = new GrpcClientLogisticaImplementation()
          .enviarUsuario(usuario);

      if (!enviado) {
        throw new ErrorLogisticaException(
            "Logística no pudo registrar el administrador."
        );
      }

      em.getTransaction().commit();

    } catch (ErrorLogisticaException e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw e;

    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      throw new RuntimeException(
          "No se pudo registrar el administrador.", e
      );
    }
  }
}
