package ar.edu.utn.frba.dds.clients.GrpcClientImplementation;

import ar.edu.utn.frba.dds.clients.GrpcClientImplementation.GrpcClient.LogisticaClient;
import ar.edu.utn.frba.dds.model.EstadoRuta;
import com.rutasServiceGrpc.grpc.RutaServiceGrpc;
import com.rutasServiceGrpc.grpc.RutasRequest;
import com.rutasServiceGrpc.grpc.RutasResponse;

public class GrpcClientRutaImplementation extends LogisticaClient {
  private final RutaServiceGrpc.RutaServiceBlockingStub stub;

  public GrpcClientRutaImplementation() {
    this.stub = RutaServiceGrpc.newBlockingStub(channel);
  }

  public RutasResponse mostrarRutas(String token) {
    return stub.mostrarRutas(RutasRequest.newBuilder()
            .setToken(token)
        .build()
    );
  }

  public RutasResponse mostrarRutasXestado(String token, EstadoRuta estado) {
    return stub.mostrarRutas(RutasRequest.newBuilder()
        .setToken(token)
        .setEstado(estado.name())
        .build()
    );
  }

  public RutasResponse mostrarRutasXcamion(String token, String patente) {
    return stub.mostrarRutas(RutasRequest.newBuilder()
        .setToken(token)
        .setPatente(patente)
        .build()
    );
  }
}
