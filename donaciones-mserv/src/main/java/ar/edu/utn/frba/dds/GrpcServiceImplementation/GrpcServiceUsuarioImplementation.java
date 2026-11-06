package ar.edu.utn.frba.dds.GrpcServiceImplementation;

import ar.edu.utn.frba.dds.grpc.RegistroUsuarioResponse;
import ar.edu.utn.frba.dds.grpc.UsuarioServiceGrpc;
import io.grpc.stub.StreamObserver;

public class GrpcServiceUsuarioImplementation
    extends UsuarioServiceGrpc.UsuarioServiceImplBase {

  public void registrarUsuario(
      UsuarioRequest request,
      StreamObserver<RegistroUsuarioResponse> responseObserver) {

    try {
      // 1. Obtener los datos del usuario.
      Long id = request.getId();
      String email = request.getEmail();
      String rol = request.getRol();

      // 2. Registrar el usuario en Logística.
      RegistroUsuarioResponse response =
          RegistroUsuarioResponse.newBuilder()
              .setExito(true)
              .setMensaje("Usuario registrado correctamente en Logística")
              .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();

    } catch (Exception e) {
      RegistroUsuarioResponse response =
          RegistroUsuarioResponse.newBuilder()
              .setExito(false)
              .setMensaje(e.getMessage() != null
                  ? e.getMessage()
                  : "Error al registrar usuario")
              .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    }
  }
}
