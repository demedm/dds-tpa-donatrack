package ar.edu.utn.frba.dds.controllers.autenticacion;

import ar.edu.utn.frba.dds.exceptions.CredencialesInvalidasException;
import ar.edu.utn.frba.dds.exceptions.UsuarioNotFoundException;
import ar.edu.utn.frba.dds.repositories.autenticacion.AutenticacionRepositorio;
import io.javalin.http.Context;

import java.util.Map;

public class AuthController {

  public void buscar(Context ctx) {

  }

  public void login(Context ctx) {
    LoginRequest request = ctx.bodyAsClass(LoginRequest.class);

    try {
      String token = AutenticacionRepositorio.Instance
          .autenticarUsuario(request.getMail(), request.getPassword());

      ctx.status(200);
      ctx.json(new LoginResponse(token));
    } catch (UsuarioNotFoundException e) {
      ctx.status(404);

      ctx.json(Map.of("mensaje", "Usuario no encontrado"));
    } catch (CredencialesInvalidasException e) {
      ctx.status(401);

      ctx.json(Map.of("mensaje", "Credenciales inválidas"));
    }
  }

  public void registro(Context ctx) {

  }

}
