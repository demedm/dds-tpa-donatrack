package ar.edu.utn.frba.dds.clients.GrpcClientImplementation;

import ar.edu.utn.frba.dds.grpc.PlanificacionServiceGrpc;
import com.authServicegrpc.grpc.AuthServiceGrpc;
import com.authServicegrpc.grpc.LoginRequest;
import com.authServicegrpc.grpc.LoginResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClientAuthImplementation {

  private final AuthServiceGrpc.AuthServiceBlockingStub stub;

  public GrpcClientAuthImplementation() {
    String host = System.getenv().getOrDefault("DONACIONES_GRPC_HOST", "localhost");
    int port = Integer.parseInt(System.getenv().getOrDefault("DONACIONES_GRPC_PORT", "9090"));

    ManagedChannel channel = ManagedChannelBuilder
        .forAddress(host, port)
        .usePlaintext()
        .build();

    this.stub = AuthServiceGrpc.newBlockingStub(channel);
  }

  public LoginResponse autenticarUsuario(String mail, String password) {
    return stub.autenticarUsuario(
        LoginRequest.newBuilder()
            .setMail(mail)
            .setPassword(password)
            .build()
    );
  }
}
