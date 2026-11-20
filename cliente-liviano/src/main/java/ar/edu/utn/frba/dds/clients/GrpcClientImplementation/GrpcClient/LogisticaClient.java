package ar.edu.utn.frba.dds.clients.GrpcClientImplementation.GrpcClient;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class LogisticaClient {
  protected String host;
  protected int port;
  protected ManagedChannel channel;

  public LogisticaClient() {
    host = System.getenv().getOrDefault("LOGISTICA_GRPC_HOST", "localhost");
    port = Integer.parseInt(System.getenv().getOrDefault("LOGISTICA_GRPC_PORT", "9091"));

    this.channel = ManagedChannelBuilder
        .forAddress(host, port)
        .usePlaintext()
        .build();
  }

  public void shutdown() {
    try {
      channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      channel.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }
}
