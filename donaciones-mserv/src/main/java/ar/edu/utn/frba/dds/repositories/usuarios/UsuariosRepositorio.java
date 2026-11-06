package ar.edu.utn.frba.dds.repositories.usuarios;

import ar.edu.utn.frba.dds.GrpcClientLogistica.GrpcClientLogisticaImplementation;
import ar.edu.utn.frba.dds.exceptions.UsuarioNotFoundException;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.DonanteRepository;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import java.net.http.HttpResponse;

public class UsuariosRepositorio implements WithSimplePersistenceUnit {
  public static final UsuariosRepositorio Instance = new UsuariosRepositorio();

  public UsuariosRepositorio() {

  }

  public void registrar(Usuario usuario, Persona donante) {
    EntityManager em = entityManager();

    try {
      em.getTransaction().begin();
      if (!DonanteRepository.Instance.registrarDonante(donante)) {
        throw new RuntimeException("Ocurrió un error al intentar registrar un nuevo donante.");
      }

      usuario.setDominioId(donante.getId());
      em.persist(usuario);
      boolean enviado = new GrpcClientLogisticaImplementation().enviarUsuario(usuario);
      if (!enviado) {
        throw new RuntimeException("Logística no pudo registrar el usuario.");
      }

      em.getTransaction().commit();

    } catch (Exception e) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }

      throw new RuntimeException(
          "No se pudo completar el registro del usuario.", e
      );
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
}
