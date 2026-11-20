package ar.edu.utn.frba.dds.clients.GrpcClientImplementation;

import ar.edu.utn.frba.dds.clients.GrpcClientImplementation.GrpcClient.LogisticaClient;
import com.planificacionServiceGrpc.grpc.DonacionAPlanificar;
import com.planificacionServiceGrpc.grpc.DonacionRechazada;
import com.planificacionServiceGrpc.grpc.EnviarDonacionesRequest;
import com.planificacionServiceGrpc.grpc.EnviarDonacionesResponse;
import com.planificacionServiceGrpc.grpc.MotivoRechazo;
import com.planificacionServiceGrpc.grpc.PlaniServiceGrpc;
import com.planificacionServiceGrpc.grpc.PlanificacionRequest;
import com.planificacionServiceGrpc.grpc.PlanificacionResponse;
import io.grpc.StatusRuntimeException;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class GrpcClientPlaniImplementation extends LogisticaClient {
  private final PlaniServiceGrpc.PlaniServiceBlockingStub stub;

  public GrpcClientPlaniImplementation() {
    this.stub = PlaniServiceGrpc.newBlockingStub(channel);
  }

  public boolean enviarDonacion(DonacionAPlanificar donacion) {
    try {
      EnviarDonacionesResponse resp = stub
          .withDeadlineAfter(5, TimeUnit.SECONDS)
          .enviarDonaciones(EnviarDonacionesRequest.newBuilder()
              .addDonaciones(donacion)
              .build());

      for (DonacionRechazada r : resp.getRechazadasList()) {
        if (r.getMotivo() == MotivoRechazo.DUPLICADA) {
          return true;   // un reintento: ya está en logística
        }
        System.err.println("Logística rechazó la donación " + r.getDonacionId()
            + ": " + r.getMotivo());
        return false;
      }
      return resp.getAceptadas() == 1;

    } catch (StatusRuntimeException e) {
      System.err.println("Fallo gRPC con logística: " + e.getStatus());
      return false;
    }
  }

  public boolean iniciarPlanificacion(List<Long> camionesIds) {
    try {
      PlanificacionRequest request = PlanificacionRequest.newBuilder()
          .addAllCamionesId(camionesIds)
          .build();

      PlanificacionResponse response =
          stub.iniciarPlanificacionManual(request);

      if (!response.getExito()) {
        System.err.println(
            "Error al iniciar planificación: " + response.getMensaje()
        );
      }

      return response.getExito();

    } catch (Exception e) {
      System.err.println(
          "Error en solicitud gRPC: " + e.getMessage()
      );
      return false;
    }
  }

}
