package ar.edu.utn.frba.dds.grpcServiceImplementation;


import ar.edu.utn.frba.dds.grpc.PlanificacionServiceGrpc;
import ar.edu.utn.frba.dds.grpc.ConfirmationPlanificacionResponse;
import ar.edu.utn.frba.dds.grpc.SolicitudPlanificacionRequest;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.repositories.EntregaRepositorio;
import io.grpc.stub.StreamObserver;
import java.util.List;

public class GrpcServiceLogisticaImplementation extends PlanificacionServiceGrpc.PlanificacionServiceImplBase {

    @Override
    public void solicitarPlanificacion(
            SolicitudPlanificacionRequest request,
            StreamObserver<ConfirmationPlanificacionResponse> responseObserver
    ) {
        try {
            List<Entrega> nuevasEntregas = request.getDonacionesList().stream()
                    .map(d -> new Entrega(d.getDireccion(), d.getIdDonacion()))
                    .toList();

            nuevasEntregas.forEach(EntregaRepositorio.Instance::registrar);

            ConfirmationPlanificacionResponse response = ConfirmationPlanificacionResponse.newBuilder()
                    .setResponseId("LOTE-RECIBIDO-OK")
                    .setEstado("ACEPTADO")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}