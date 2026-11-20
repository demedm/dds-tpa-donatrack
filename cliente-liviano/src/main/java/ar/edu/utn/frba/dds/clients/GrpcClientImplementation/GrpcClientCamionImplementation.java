package ar.edu.utn.frba.dds.clients.GrpcClientImplementation;

import ar.edu.utn.frba.dds.clients.GrpcClientImplementation.GrpcClient.LogisticaClient;
import ar.edu.utn.frba.dds.model.EstadoCamion;
import com.camionesServiceGrpc.grpc.Camion;
import com.camionesServiceGrpc.grpc.CamionRequest;
import com.camionesServiceGrpc.grpc.CamionResponse;
import com.camionesServiceGrpc.grpc.CamionesRequest;
import com.camionesServiceGrpc.grpc.CamionesResponse;
import com.camionesServiceGrpc.grpc.CamionesServiceGrpc;
import java.util.concurrent.TimeUnit;

public class GrpcClientCamionImplementation extends LogisticaClient {
  private final CamionesServiceGrpc.CamionesServiceBlockingStub stub;

  public GrpcClientCamionImplementation() {
    this.stub = CamionesServiceGrpc.newBlockingStub(channel);
  }

  public CamionesResponse mostrarCamiones(String token) {
    return stub.withDeadlineAfter(5, TimeUnit.SECONDS)
        .mostrarCamiones(CamionesRequest.newBuilder()
        .setToken(token)
        .build()
    );
  }

  public CamionesResponse mostrarCamionesXestado(String token, EstadoCamion estado) {
    return stub.mostrarCamiones(CamionesRequest.newBuilder()
        .setToken(token)
        .setEstado(estado.name())
        .build()
    );
  }

  public CamionResponse mostrarCamionXid(long id) {
    return stub.mostrarCamion(CamionRequest.newBuilder()
        .setId(id)
        .build()
    );
  }

  public CamionResponse mostrarCamionXpatente(String patente) {
    return stub.mostrarCamion(CamionRequest.newBuilder()
        .setPatente(patente)
        .build()
    );
  }
}
