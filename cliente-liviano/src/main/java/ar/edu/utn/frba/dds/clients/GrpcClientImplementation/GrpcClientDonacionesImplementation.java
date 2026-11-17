package ar.edu.utn.frba.dds.clients.GrpcClientImplementation;

import com.servDonaciones.donacionesServicegrpc.grpc.DonacionesServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GrpcClientDonacionesImplementation {
  private final DonacionesServiceGrpc.DonacionesServiceBlockingStub stub;
  private final ManagedChannel channel;

  public GrpcClientDonacionesImplementation() {
    String host = System.getenv().getOrDefault("DONACIONES_GRPC_HOST", "localhost");
    int port = Integer.parseInt(
        System.getenv().getOrDefault("DONACIONES_GRPC_PORT", "9090"));

    this.channel = ManagedChannelBuilder
        .forAddress(host, port)
        .usePlaintext()
        .build();

    this.stub = DonacionesServiceGrpc.newBlockingStub(channel);
  }
}
