package ar.edu.utn.frba.dds.grpcServiceImplementation;


import ar.edu.utn.frba.dds.grpc.*;
import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.Ubicacion;
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
    @Override
    public void obtenerEntregasEnCurso(EntregasEnCursoRequest request,
                                       StreamObserver<EntregasEnCursoResponse> responseObserver) {
        EntregaRepositorio.Instance.entityManager().clear();
        List<Entrega> entregas = EntregaRepositorio.Instance
                .buscarEnTrasladoDeEntidad(request.getEntidadId());

        EntregasEnCursoResponse.Builder responseBuilder = EntregasEnCursoResponse.newBuilder();

        for (Entrega entrega : entregas) {
            double lat = -34.6037;
            double lng = -58.3816;

            if (entrega.getRuta() != null && entrega.getRuta().getCamion() != null) {
                Camion camion = entrega.getRuta().getCamion();

                Ubicacion ubicacion = EntregaRepositorio.Instance.entityManager()
                        .createQuery("from Ubicacion u where u.camion.id = :camionId", Ubicacion.class)
                        .setParameter("camionId", camion.getId())
                        .getResultStream()
                        .findFirst()
                        .orElse(null);

                if (ubicacion != null && ubicacion.getLatitud() != null && ubicacion.getLongitud() != null) {
                    lat = ubicacion.getLatitud();
                    lng = ubicacion.getLongitud();
                }
            }

            responseBuilder.addEntregas(
                    EntregaEnCursoDto.newBuilder()
                            .setId(entrega.getId() != null ? entrega.getId() : 0L)
                            .setDonacionId(entrega.getDonacionId() != null ? entrega.getDonacionId() : 0L)
                            .setDireccion(entrega.getDireccion() != null ? entrega.getDireccion() : "")
                            .setEstado(entrega.getEstado() != null ? entrega.getEstado().name() : "EN_TRASLADO")
                            .setLatitudCamion(lat)
                            .setLongitudCamion(lng)
                            .build()
            );
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}