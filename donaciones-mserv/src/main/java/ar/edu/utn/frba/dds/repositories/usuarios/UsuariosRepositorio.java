package ar.edu.utn.frba.dds.repositories.usuarios;

import ar.edu.utn.frba.dds.exceptions.UsuarioNotFoundException;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

public class UsuariosRepositorio implements WithSimplePersistenceUnit {
  public static final UsuariosRepositorio Instance = new UsuariosRepositorio();

  public UsuariosRepositorio() {

  }

  public void registrar(Usuario usuario) {
    entityManager().getTransaction().begin();
    entityManager().persist(usuario);
    entityManager().getTransaction().commit();
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
