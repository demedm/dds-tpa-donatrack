package ar.edu.utn.frba.dds.clients.GrpcClientImplementation.GrpcClient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class DonacionClient {
  protected String host;
  protected int port;
  protected ManagedChannel channel;

  public DonacionClient() {
    this.host = System.getenv().getOrDefault("DONACIONES_GRPC_HOST", "localhost");
    this.port = Integer.parseInt(System.getenv().getOrDefault("DONACIONES_GRPC_PORT", "9090"));

    this.channel = ManagedChannelBuilder
        .forAddress(host, port)
        .usePlaintext()
        .build();
  }
}
