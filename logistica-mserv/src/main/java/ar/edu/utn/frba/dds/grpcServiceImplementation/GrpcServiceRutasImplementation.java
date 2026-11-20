package ar.edu.utn.frba.dds.grpcServiceImplementation;

import ar.edu.utn.frba.dds.model.EstadoRuta;
import ar.edu.utn.frba.dds.repositories.EntregaRepositorio;
import ar.edu.utn.frba.dds.repositories.RutaRepositorio;
import ar.edu.utn.frba.dds.repositories.UsuarioRepositorio;
import ar.edu.utn.frba.dds.repositories.autenticacion.JwtRepositorio;
import ar.edu.utn.frba.dds.repositories.autenticacion.Rol;
import ar.edu.utn.frba.dds.scripts.dto.autenticacion.UsuarioLogueado;
import com.camionesServiceGrpc.grpc.CamionesResponse;
import com.rutasServiceGrpc.grpc.Entrega;
import com.rutasServiceGrpc.grpc.ListEntrega;
import com.rutasServiceGrpc.grpc.ListRuta;
import com.rutasServiceGrpc.grpc.Ruta;
import com.rutasServiceGrpc.grpc.RutaServiceGrpc;
import com.rutasServiceGrpc.grpc.RutasRequest;
import com.rutasServiceGrpc.grpc.RutasResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.List;

public class GrpcServiceRutasImplementation extends RutaServiceGrpc.RutaServiceImplBase {

  @Override
  public void mostrarRutas(RutasRequest request, StreamObserver<RutasResponse> responseObserver) {
    try {
      UsuarioLogueado usuarioLogueado = JwtRepositorio.Instance.obtenerUsuario(request.getToken());
      UsuarioRepositorio.Instance.buscarPorId(usuarioLogueado.getId());
      if (usuarioLogueado.getRol() != Rol.ADMIN) {
        responseObserver.onError(Status.PERMISSION_DENIED
            .withDescription("Usuario no tiene permisos suficientes.")
            .asRuntimeException()
        );
        return;
      }
      List<ar.edu.utn.frba.dds.model.Ruta> rutas = RutaRepositorio.Instance.mostrarTodos();
      ListRuta.Builder listRutaBuilder = ListRuta.newBuilder();
      for (ar.edu.utn.frba.dds.model.Ruta ruta : rutas) {
        listRutaBuilder.addRutas(toProto(ruta));
      }

      RutasResponse response = RutasResponse.newBuilder()
          .setExito(true)
          .setMensaje("OK")
          .setRuta(listRutaBuilder.build())
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

  private Ruta toProto(ar.edu.utn.frba.dds.model.Ruta ruta) {
    ListEntrega.Builder entregas = ListEntrega.newBuilder();

    // Ajustá getEntregas() / getParadas() según tu modelo
    for (ar.edu.utn.frba.dds.model.Entrega e : ruta.getEntregas()) {
      entregas.addEntrega(Entrega.newBuilder()
          .setId(e.getId())
          .setDireccion(e.getDireccion() == null ? "" : e.getDireccion())
          .setFechaVencimiento(e.getFechaVencimiento() != null ?
              e.getFechaVencimiento().toString() : "")
          .setDonacionId(e.getDonacionId())
          .setBeneficiarioId(e.getEntidadBeneficiaria().getId())
          .setEstado(e.getEstado().name())
          .build());
    }

    return Ruta.newBuilder()
        .setId(ruta.getId())
        .setEntregas(entregas.build())
        .setCamionId(ruta.getCamion().getId())
        .setEstado(ruta.getEstado().name())
        .build();
  }

}
