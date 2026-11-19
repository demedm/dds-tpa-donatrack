package ar.edu.utn.frba.dds.clients.GrpcClientImplementation;

import ar.edu.utn.frba.dds.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClientGestionDonacionesImplementation  {
  private final GestionDonacionesServiceGrpc.GestionDonacionesServiceBlockingStub stub;

  private final ManagedChannel channel;

  public GrpcClientGestionDonacionesImplementation() {
    this.channel = ManagedChannelBuilder
        .forAddress("localhost", 9090)
        .usePlaintext()
        .build();

    this.stub = GestionDonacionesServiceGrpc.newBlockingStub(channel);
  }

  //pantalla 14
  public boolean marcarVencida(Long segmentadaId){
    try{
      ResultadoOperacion respuesta = stub.marcarVencida(
          SegmentadaIdRequest.newBuilder().setSegmentadaId(segmentadaId).build());

      if(!respuesta.getExito()) {
        System.err.println("No vencio la donación " + segmentadaId + ": " + respuesta.getMensaje());
      }
      return respuesta.getExito();
    }catch (Exception e){
      System.err.println("Fallo conexion gRPC con donaciones (9090) marcarVencida: " + e.getMessage());
      return false;
    }
  }

}
