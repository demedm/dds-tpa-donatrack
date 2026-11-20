package ar.edu.utn.frba.dds.grpcServiceImplementation;

import ar.edu.utn.frba.dds.repositories.CamionRepositorio;
import ar.edu.utn.frba.dds.repositories.UsuarioRepositorio;
import ar.edu.utn.frba.dds.repositories.autenticacion.JwtRepositorio;
import ar.edu.utn.frba.dds.repositories.autenticacion.Rol;
import ar.edu.utn.frba.dds.scripts.dto.autenticacion.UsuarioLogueado;
import com.camionesServiceGrpc.grpc.Camion;
import com.camionesServiceGrpc.grpc.CamionRequest;
import com.camionesServiceGrpc.grpc.CamionResponse;
import com.camionesServiceGrpc.grpc.CamionesRequest;
import com.camionesServiceGrpc.grpc.CamionesResponse;
import com.camionesServiceGrpc.grpc.CamionesServiceGrpc;
import com.camionesServiceGrpc.grpc.ListCamion;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.List;

public class GrpcServiceCamionImplementation extends CamionesServiceGrpc.CamionesServiceImplBase {

  @Override
  public void mostrarCamion(CamionRequest request,
                            StreamObserver<CamionResponse> responseObserver) {
    try {
      ar.edu.utn.frba.dds.model.Camion camion;
      Camion.Builder builder = Camion.newBuilder();
      if (!request.getPatente().isBlank()) {
        camion = CamionRepositorio.Instance.buscarPorPatente(request.getPatente());
      } else {
        camion = CamionRepositorio.Instance.buscarPorId(request.getId());
      }
      CamionResponse response = CamionResponse.newBuilder()
          .setCamion(toProto(camion))
          .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(Status.INTERNAL
          .withDescription(e.getMessage())
          .asRuntimeException());
      throw new RuntimeException(e);
    }
  }

  @Override
  public void mostrarCamiones(CamionesRequest request,
                              StreamObserver<CamionesResponse> responseObserver) {
    try {
      if (autorizado(request.getToken())) {
        responseObserver.onError(Status.PERMISSION_DENIED
            .withDescription("Usuario no tiene permisos suficientes.")
            .asRuntimeException()
        );
        return;
      }
      List<ar.edu.utn.frba.dds.model.Camion> camiones = CamionRepositorio.Instance.mostrarTodos();
      ListCamion.Builder builder = ListCamion.newBuilder();
      for (ar.edu.utn.frba.dds.model.Camion camion : camiones) {
        builder.addCamiones(toProto(camion));
      }

      CamionesResponse response = CamionesResponse.newBuilder()
          .setExito(true)
          .setMensaje("OK")
          .setCamiones(builder.build())
          .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      responseObserver.onError(Status.INTERNAL
          .withDescription(e.getMessage())
          .withCause(e)
          .asRuntimeException());
      throw new RuntimeException(e);
    }
  }

  private boolean autorizado(String token) {
    UsuarioLogueado usuarioLogueado = JwtRepositorio.Instance.obtenerUsuario(token);
    UsuarioRepositorio.Instance.buscarPorId(usuarioLogueado.getId());
    return usuarioLogueado.getRol() != Rol.ADMIN;
  }

  private Camion toProto(ar.edu.utn.frba.dds.model.Camion c) {

    return com.camionesServiceGrpc.grpc.Camion.newBuilder()
        .setId(c.getId())
        .setPatente(c.getPatente())
        .setCapacVolumen(c.getCapacidadVolumen())
        .setCapacCarga(c.getCapacidadCarga())
        .setAltura(c.getAltura())
        .setEstado(c.getEstado() == null ? "" : c.getEstado().name())
        .setNombreChofer("Nombre Chofer")
        .build();
  }
}
