package ar.edu.utn.frba.dds.GrpcServiceImplementation;

import ar.edu.utn.frba.dds.dto.autenticacion.RegistroRequest;
import ar.edu.utn.frba.dds.exceptions.ErrorLogisticaException;
import ar.edu.utn.frba.dds.exceptions.MailAlreadyRegisteredException;
import ar.edu.utn.frba.dds.model.donantes.*;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.medioscontacto.Telefono;
import ar.edu.utn.frba.dds.model.notificaciones.EventoNotificacionService;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.autenticacion.AutenticacionRepositorio;
import ar.edu.utn.frba.dds.repositories.usuarios.UsuariosRepositorio;
import com.registroServicegrpc.grpc.*;
import io.grpc.stub.StreamObserver;

import java.util.List;

public class GrpcServiceRegistroImplementation extends RegistroServiceGrpc.RegistroServiceImplBase {
  @Override
  public void registrarPersona(RegistroPersonaRequest request,
                               StreamObserver<RegistroResponse> responseObserver) {
    try {
      // el mail de la persona es también su usuario de login
      Usuario usuario = AutenticacionRepositorio.Instance.validarDatosRegistro(
              request.getMailDonante(), request.getPassword(), "DONANTE");

      Persona donante;
      if (request.getTipoPersona().equals(TipoPersona.FISICA.name())) {
        donante = new PersonaFisica();
      } else {
        donante = new PersonaJuridica();
      }
      donante.setMail(new Mail(request.getMailDonante()));
      donante.setNombreIdentificador(request.getNombreIdentificador());
      donante.setTelefono(new Telefono(request.getTelefonoDonante()));
      donante.setMedioPreferido(MedioPreferido.strTo(request.getMedioPreferido()));

      // crea la Persona y deja usuario.dominioId = persona.id
      UsuariosRepositorio.Instance.registrarUsuarioDeDonante(usuario, donante);

      try { // opcional, igual que hacía AuthController
        EventoNotificacionService.Instance.notificarBienvenida(donante);
      } catch (Exception e) {
        System.err.println("[Bienvenida] No se pudo notificar: " + e.getMessage());
      }

      responder(responseObserver, true, "Registro exitoso.");
    } catch (MailAlreadyRegisteredException e) {
      responder(responseObserver, false, e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      responder(responseObserver, false,
              "Ocurrió un error en el registro. Vuelva a intentarlo más tarde.");
    }
  }

  @Override
  public void registrarEntidad(RegistroBeneficiariaRequest request,
                               StreamObserver<RegistroResponse> responseObserver) {
    try {
      Usuario usuario = AutenticacionRepositorio.Instance.validarDatosRegistro(
              request.getMailContacto(), request.getPassword(), "BENEFICIARIO");

      EntidadBeneficiaria entidad = new EntidadBeneficiaria(
              request.getDireccion(),
              List.of(new Mail(request.getMailContacto())),
              request.getRazonSocial(),
              new Telefono(request.getTelefonoBeneficiario()));

      RegistroRequest datos = new RegistroRequest();
      datos.setRazonSocial(request.getRazonSocial());
      datos.setDireccion(request.getDireccion());

      UsuariosRepositorio.Instance.registrarUsuarioDeBeneficiario(usuario, entidad, datos);

      responder(responseObserver, true, "Registro exitoso.");
    } catch (MailAlreadyRegisteredException e) {
      responder(responseObserver, false, e.getMessage());
    } catch (ErrorLogisticaException e) {
      e.printStackTrace();
      responder(responseObserver, false,
              "No se pudo completar el registro porque Logística no respondió.");
    } catch (Exception e) {
      e.printStackTrace();
      responder(responseObserver, false,
              "Ocurrió un error en el registro. Vuelva a intentarlo más tarde.");
    }
  }
  @Override
  public void registrarUsuario(RegistroUsuarioRequest request,
                               StreamObserver<RegistroResponse> responseObserver) {
    try {
      Usuario usuario = AutenticacionRepositorio.Instance.validarDatosRegistro(request.getMail(),
          request.getPassword(), request.getRol());
      UsuariosRepositorio.Instance.registrarUsuario(usuario);

      RegistroResponse response = RegistroResponse.newBuilder()
          .setExito(true)
          .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (MailAlreadyRegisteredException e) {
      RegistroResponse response = RegistroResponse.newBuilder()
          .setExito(false)
          .setMensaje(e.getMessage())
          .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (ErrorLogisticaException e) {
      RegistroResponse response = RegistroResponse.newBuilder()
          .setExito(false)
          .setMensaje("Ocurrió un error en el registro. Vuelva a intentarlo más tarde.")
          .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
      throw new ErrorLogisticaException(e.getMessage());
    } catch (Exception e) {
      RegistroResponse response = RegistroResponse.newBuilder()
          .setExito(false)
          .setMensaje("Ocurrió un error en el registro. Vuelva a intentarlo más tarde.")
          .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
      throw new RuntimeException(e);
    }

  }
  private void responder(StreamObserver<RegistroResponse> observer, boolean exito, String mensaje) {
    observer.onNext(RegistroResponse.newBuilder().setExito(exito).setMensaje(mensaje).build());
    observer.onCompleted();
  }
}
