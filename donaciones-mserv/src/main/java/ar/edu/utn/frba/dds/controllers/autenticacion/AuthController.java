package ar.edu.utn.frba.dds.controllers.autenticacion;

import ar.edu.utn.frba.dds.dto.autenticacion.LoginRequest;
import ar.edu.utn.frba.dds.dto.autenticacion.LoginResponse;
import ar.edu.utn.frba.dds.dto.autenticacion.RegistroRequest;
import ar.edu.utn.frba.dds.exceptions.CredencialesInvalidasException;
import ar.edu.utn.frba.dds.exceptions.ErrorLogisticaException;
import ar.edu.utn.frba.dds.exceptions.MailAlreadyRegisteredException;
import ar.edu.utn.frba.dds.exceptions.UsuarioNotFoundException;
import ar.edu.utn.frba.dds.model.donantes.MedioPreferido;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.donantes.PersonaFisica;
import ar.edu.utn.frba.dds.model.donantes.PersonaJuridica;
import ar.edu.utn.frba.dds.model.donantes.TipoPersona;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.medioscontacto.Telefono;
import ar.edu.utn.frba.dds.model.notificaciones.EventoNotificacionService;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.autenticacion.AutenticacionRepositorio;
import ar.edu.utn.frba.dds.repositories.usuarios.UsuariosRepositorio;
import io.javalin.http.Context;

import java.util.List;
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

      if (request.getRol().equals(Rol.DONANTE.name())) {
        Persona donante = nuevaPersona(request);
        UsuariosRepositorio.Instance.registrarUsuarioDeDonante(usuario, donante);
      } else if (request.getRol().equals(Rol.BENEFICIARIO.name())) {
        EntidadBeneficiaria beneficiaria = nuevaEntidad(request);
        UsuariosRepositorio.Instance.registrarUsuarioDeBeneficiario(usuario, beneficiaria, request);
      } else {
        UsuariosRepositorio.Instance.registrarUsuarioDeAdmin(usuario);
      }
      UsuariosRepositorio.Instance.registrar(usuario);
      try {
        EventoNotificacionService.Instance.notificarBienvenida(new Mail(usuario.getEmail()));
      } catch (Exception e) {
        System.err.println("[Bienvenida] No se pudo notificar a "
            + usuario.getEmail() + ": " + e.getMessage());
      }
      ctx.status(201);
    } catch (MailAlreadyRegisteredException e) {
      ctx.status(409);
    } catch (ErrorLogisticaException e) {
      e.printStackTrace();
      ctx.status(502);
    } catch (Exception e) {
      e.printStackTrace(); // debugueo :o
      ctx.status(400);
    }

  }

  private Persona nuevaPersona(RegistroRequest request) {
    if (request.getTipoPersona().equals(TipoPersona.FISICA.name())) {
      PersonaFisica persona = new PersonaFisica();
      persona.setMail(new Mail(request.getEmail()));
      persona.setNombreIdentificador(request.getNombreIdentificador());
      persona.setTelefono(new Telefono(request.getTelefonoDonante()));
      persona.setMedioPreferido(MedioPreferido.strTo(request.getMedioPreferido()));
      return persona;
    } else {
      PersonaJuridica persona = new PersonaJuridica();
      persona.setMail(new Mail(request.getEmail()));
      persona.setNombreIdentificador(request.getNombreIdentificador());
      persona.setTelefono(new Telefono(request.getTelefonoDonante()));
      persona.setMedioPreferido(MedioPreferido.strTo(request.getMedioPreferido()));
      return persona;
    }
  }

  private EntidadBeneficiaria nuevaEntidad(RegistroRequest request) {
    return new EntidadBeneficiaria(request.getDireccion(),
        List.of(new Mail(request.getMailContacto())),
        request.getRazonSocial(),
        new Telefono(request.getTelefonoBeneficiario())
    );
  }

}
