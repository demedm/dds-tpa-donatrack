package ar.edu.utn.frba.dds.repositories.autenticacion;

import ar.edu.utn.frba.dds.exceptions.CredencialesInvalidasException;
import ar.edu.utn.frba.dds.exceptions.UsuarioNotFoundException;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.usuarios.UsuariosRepositorio;
import org.mindrot.jbcrypt.BCrypt;

public class AutenticacionRepositorio {
  public static final AutenticacionRepositorio Instance = new AutenticacionRepositorio();

  public String autenticarUsuario(String email, String password) {
    Usuario usuario = UsuariosRepositorio.Instance.buscarXmail(email);

    if (usuario == null) {
      throw new UsuarioNotFoundException(email);
    } else if (!BCrypt.checkpw(password, usuario.getHashPassword())) {
      throw new CredencialesInvalidasException(email);
    }

    return JwtRepositorio.Instance.generarToken(usuario);
  }

}
