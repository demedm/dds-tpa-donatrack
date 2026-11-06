package ar.edu.utn.frba.dds.GrpcServiceImplementation;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.Estado.EstadoDonacion;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;
import com.donacionesServicegrpc.grpc.ActualizacionEstadoRequest;
import com.donacionesServicegrpc.grpc.ActualizacionEstadoResponse;
import com.donacionesServicegrpc.grpc.donacionesServiceGrpc;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.grpc.stub.StreamObserver;

public class GrpcServiceDonacionImplementation
        extends donacionesServiceGrpc.donacionesServiceImplBase
        implements WithSimplePersistenceUnit {

    @Override
    public void actualizarEstadoDonacion(
            ActualizacionEstadoRequest request,
            StreamObserver<ActualizacionEstadoResponse> responseObserver
    ) {
        try {
            Long id = request.getDonacionId();
            String nuevoEstadoStr = request.getNuevoEstado();
            String motivoFalla = request.getMotivo(); // Si lo envías en la request

            withTransaction(() -> {
                DonacionSegmentada segmentada = DonacionesRepository.Instance.findSegmentadaById(id);

                if (segmentada == null) {
                    throw new RuntimeException("Donacion segmentada " + id + " no encontrada");
                }

                // 1. Seteamos el motivo de falla si viene informado
                if (motivoFalla != null && !motivoFalla.isBlank()) {
                    segmentada.setJustificacionFallo(motivoFalla);
                }

                // 2. Pasamos la cadena del proto directamente (ej: "ENTREGADA", "EN_TRASLADO", "FALLIDA")
                segmentada.cambiarEstado(nuevoEstadoStr);
            });

            ActualizacionEstadoResponse response = ActualizacionEstadoResponse.newBuilder()
                    .setExito(true)
                    .setMensaje("Estado actualizado correctamente en Donaciones")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            System.err.println("❌ Error actualizando donación desde gRPC: " + e.getMessage());

            ActualizacionEstadoResponse response = ActualizacionEstadoResponse.newBuilder()
                    .setExito(false)
                    .setMensaje(e.getMessage())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}