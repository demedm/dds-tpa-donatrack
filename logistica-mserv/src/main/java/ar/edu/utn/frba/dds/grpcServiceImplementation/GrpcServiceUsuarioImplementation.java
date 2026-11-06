package ar.edu.utn.frba.dds.grpcServiceImplementation;

import ar.edu.utn.frba.dds.grpc.RegistroUsuarioResponse;
import ar.edu.utn.frba.dds.grpc.UsuarioRequest;
import ar.edu.utn.frba.dds.grpc.UsuarioServiceGrpc;
import ar.edu.utn.frba.dds.model.usuarios.Administrador;
import ar.edu.utn.frba.dds.model.usuarios.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.UsuarioRepositorio;
import io.grpc.stub.StreamObserver;

public class GrpcServiceUsuarioImplementation
    extends UsuarioServiceGrpc.UsuarioServiceImplBase {

  @Override
  public void registrarUsuario(
      UsuarioRequest request,
      StreamObserver<RegistroUsuarioResponse> responseObserver
  ) {
    try {
      Long idDonaciones = request.getId();

      Usuario existente = UsuarioRepositorio.Instance.buscarXidDonaciones(idDonaciones);

      if (existente != null) {
        responder(responseObserver, true,
            "El usuario ya estaba registrado en Logística.");
        return;
      }

      Usuario usuario = construirUsuario(request);
      UsuarioRepositorio.Instance.registrar(usuario);

      responder(responseObserver, true,
          "Usuario registrado correctamente en Logística.");

    } catch (Exception e) {
      e.printStackTrace();

      responder(responseObserver, false,
          e.getMessage() != null
              ? e.getMessage()
              : "No se pudo registrar el usuario.");
    }
  }

  private Usuario construirUsuario(UsuarioRequest request) {
    Usuario usuario;

    switch (request.getRol()) {
      case "BENEFICIARIO":
        usuario = new EntidadBeneficiaria(
            request.getEmail(),
            request.getDireccion(),
            request.getRazonSocial()
        );
        break;

      case "ADMIN":
        usuario = new Administrador(request.getEmail());
        break;

      default:
        throw new IllegalArgumentException(
            "Rol no soportado por Logística: " + request.getRol()
        );
    }

    usuario.setIdDonaciones(request.getId());

    return usuario;
  }

  private void responder(
      StreamObserver<RegistroUsuarioResponse> observer,
      boolean exito,
      String mensaje
  ) {
    RegistroUsuarioResponse response =
        RegistroUsuarioResponse.newBuilder()
            .setExito(exito)
            .setMensaje(mensaje)
            .build();

    observer.onNext(response);
    observer.onCompleted();
  }
}
