package ar.edu.utn.frba.dds.GrpcServiceImplementation;

import ar.edu.utn.frba.dds.exceptions.CredencialesInvalidasException;
import ar.edu.utn.frba.dds.exceptions.UsuarioNotFoundException;
import ar.edu.utn.frba.dds.repositories.autenticacion.AutenticacionRepositorio;
import com.authServicegrpc.grpc.AuthServiceGrpc;
import com.authServicegrpc.grpc.LoginRequest;
import com.authServicegrpc.grpc.LoginResponse;
import io.grpc.stub.StreamObserver;

public class GrpcServiceAuthImplementation extends AuthServiceGrpc.AuthServiceImplBase {

  @Override
  public void autenticarUsuario(LoginRequest request,
                                StreamObserver<LoginResponse> responseObserver) {
    try {
      String token = AutenticacionRepositorio.Instance
          .autenticarUsuario(request.getMail(), request.getPassword());

      LoginResponse response = LoginResponse.newBuilder()
          .setExito(true)
          .setMensaje("Login exitoso.")
          .setToken(token)
          .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (UsuarioNotFoundException e) {
      LoginResponse response = LoginResponse.newBuilder()
          .setExito(false)
          .setMensaje("Usuario no encontrado.")
          .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (CredencialesInvalidasException e) {
      LoginResponse response = LoginResponse.newBuilder()
          .setExito(false)
          .setMensaje("Credenciales inválidas.")
          .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      LoginResponse response = LoginResponse.newBuilder()
          .setExito(false)
          .setMensaje("Ocurrió un error al intentar el login.")
          .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
      throw new RuntimeException(e);
    }
  }
}
