package ar.edu.utn.frba.dds.GrpcServiceImplementation;

import ar.edu.utn.frba.dds.grpc.ResultadoOperacion;
import ar.edu.utn.frba.dds.grpc.GestionDonacionesServiceGrpc;
import ar.edu.utn.frba.dds.grpc.SegmentadaIdRequest;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;
import io.grpc.stub.StreamObserver;

public class GrpcServiceGestionDonacionesImplementation
    extends GestionDonacionesServiceGrpc.GestionDonacionesServiceImplBase {

    private final DonacionesRepository donacionesRepo = DonacionesRepository.Instance;

    @Override
    public void marcarVencida(SegmentadaIdRequest request, StreamObserver<
            ResultadoOperacion> responseObserver) {

        try{
            DonacionSegmentada segmentada = donacionesRepo.findSegmentadaById(request.getSegmentadaId());

            if(segmentada == null){
                responder(responseObserver, false, "Donacion segmentada " + request.getSegmentadaId() + " no encontrada");
                return;
            }

            donacionesRepo.withTransaction(() ->
                    segmentada.cambiarEstado("VENCIDA"));

            responder(responseObserver, true, "Donación marcada como vencida");

        }catch (Exception e){
            e.printStackTrace();
            responder(responseObserver,false, "No se pudo marcar como vencida: " +
                    e.getMessage());
        }

    }

    private void responder(StreamObserver<ResultadoOperacion> responseObserver, boolean exito, String mensaje){
        responseObserver.onNext(ResultadoOperacion.newBuilder()
                .setExito(exito)
                .setMensaje(mensaje)
                .build());
        responseObserver.onCompleted();
    }
}
