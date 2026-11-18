package ar.edu.utn.frba.dds.GrpcServiceImplementation;

import ar.edu.utn.frba.dds.exceptions.ErrorLogisticaException;
import ar.edu.utn.frba.dds.exceptions.MailAlreadyRegisteredException;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.autenticacion.AutenticacionRepositorio;
import ar.edu.utn.frba.dds.repositories.usuarios.UsuariosRepositorio;
import com.registroServicegrpc.grpc.RegistroResponse;
import com.registroServicegrpc.grpc.RegistroServiceGrpc;
import com.registroServicegrpc.grpc.RegistroUsuarioRequest;
import io.grpc.stub.StreamObserver;

public class GrpcServiceRegistroImplementation extends RegistroServiceGrpc.RegistroServiceImplBase {

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
}
