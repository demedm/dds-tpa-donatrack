package ar.edu.utn.frba.dds.controllers.autenticacion;

import ar.edu.utn.frba.dds.dto.autenticacion.LoginRequest;
import ar.edu.utn.frba.dds.dto.autenticacion.LoginResponse;
import ar.edu.utn.frba.dds.dto.autenticacion.RegistroRequest;
import ar.edu.utn.frba.dds.exceptions.CredencialesInvalidasException;
import ar.edu.utn.frba.dds.exceptions.MailAlreadyRegisteredException;
import ar.edu.utn.frba.dds.exceptions.UsuarioNotFoundException;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.autenticacion.AutenticacionRepositorio;
import ar.edu.utn.frba.dds.repositories.usuarios.UsuariosRepositorio;
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
    try {
      RegistroRequest request = ctx.bodyAsClass(RegistroRequest.class);

      Usuario usuario = AutenticacionRepositorio.Instance.validarDatos(request);
      UsuariosRepositorio.Instance.registrar(usuario);
      ctx.status(201);
    } catch (MailAlreadyRegisteredException e) {
      ctx.status(409);
    } catch (Exception e) {
      ctx.status(400);
    }

  }

}
