package ar.edu.utn.frba.dds.GrpcClientDonaciones;

import com.donacionesServicegrpc.grpc.ActualizacionEstadoRequest;
import com.donacionesServicegrpc.grpc.ActualizacionEstadoResponse;
import com.donacionesServicegrpc.grpc.donacionesServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class GrpcClientDonacionesImplementation {
    private final donacionesServiceGrpc.donacionesServiceBlockingStub stub;
    private final ManagedChannel channel;

    public GrpcClientDonacionesImplementation() {
        this.channel = ManagedChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext() // Sin cifrado SSL para entorno local/desarrollo
                .build();

        this.stub = donacionesServiceGrpc.newBlockingStub(channel);
    }

    public boolean notificarCambioEstado(Long idDonacion, String nuevoEstado) {
        try {
            ActualizacionEstadoRequest request = ActualizacionEstadoRequest.newBuilder()
                    .setDonacionId(idDonacion)
                    .setNuevoEstado(nuevoEstado)
                    .build();

            ActualizacionEstadoResponse response = stub.actualizarEstadoDonacion(request);

            if (!response.getExito()) {
                System.err.println("-> ¡ERROR! Donaciones rechazó el aviso: " + response.getMensaje());
                return false;
            }

            System.out.println("-> Aviso a Donaciones: Donacion " + idDonacion + " actualizada a " + nuevoEstado);
            return true;

        } catch (StatusRuntimeException e) {
            System.err.println("-> ¡ERROR! Error de comunicación gRPC: " + e.getStatus().getDescription());
            return false;
        } catch (Exception e) {
            System.err.println("Error avisando a Donaciones estado " + nuevoEstado + ": " + e.getMessage());
            return false;
        }
    }

    public boolean notificarFallaDeEntrega(Long idDonacion, String motivo, boolean replanificable) {
        try {
            ActualizacionEstadoRequest request = ActualizacionEstadoRequest.newBuilder()
                    .setDonacionId(idDonacion)
                    .setNuevoEstado("FALLIDA")
                    .setMotivo(motivo)
                    .setReplanificable(replanificable)
                    .build();

            ActualizacionEstadoResponse response = stub.actualizarEstadoDonacion(request);

            if (!response.getExito()) {
                System.err.println("-> ¡ERROR! Donaciones rechazó el aviso de falla: " + response.getMensaje());
                return false;
            }

            System.out.println("-> Aviso a Donaciones: Donacion " + idDonacion + " FALLIDA (" + motivo + ")");
            return true;

        } catch (StatusRuntimeException e) {
            System.err.println("-> ¡ERROR! Error de comunicación gRPC: " + e.getStatus().getDescription());
            return false;
        } catch (Exception e) {
            System.err.println("Error avisando a Donaciones falla: " + e.getMessage());
            return false;
        }
    }
}
