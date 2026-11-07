package ar.edu.utn.frba.dds.GrpcClientDonaciones;

import com.donacionesServicegrpc.grpc.ActualizacionEstadoRequest;
import com.donacionesServicegrpc.grpc.ActualizacionEstadoResponse;
import com.donacionesServicegrpc.grpc.donacionesServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.Ruta;
import io.grpc.StatusRuntimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GrpcClientDonacionesImplementation {
    private static final DateTimeFormatter FORMATO_FECHA =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final donacionesServiceGrpc.donacionesServiceBlockingStub stub;
    private final ManagedChannel channel;

    public GrpcClientDonacionesImplementation() {
        String host = System.getenv().getOrDefault("DONACIONES_GRPC_HOST", "localhost");
        int port = Integer.parseInt(
            System.getenv().getOrDefault("DONACIONES_GRPC_PORT", "9090"));

        this.channel = ManagedChannelBuilder
            .forAddress(host, port)
            .usePlaintext()
            .build();

        this.stub = donacionesServiceGrpc.newBlockingStub(channel);
    }

    public boolean notificarCambioEstado(Long idDonacion, String nuevoEstado) {
        return enviar(
            ActualizacionEstadoRequest.newBuilder()
                .setDonacionId(idDonacion)
                .setNuevoEstado(nuevoEstado)
                .build(),
            "Donacion " + idDonacion + " -> " + nuevoEstado);
    }

    public boolean notificarInicioDeRuta(Entrega entrega, Ruta ruta) {
        Long rutaId = ruta != null ? ruta.getId() : null;

        ActualizacionEstadoRequest.Builder request = ActualizacionEstadoRequest.newBuilder()
            .setDonacionId(entrega.getDonacionId())
            .setNuevoEstado("EN_TRASLADO");

        if (rutaId != null) {
            request.setRutaId(String.valueOf(rutaId));
            request.setUrlMapa(urlDeSeguimiento(rutaId));
        }

        return enviar(request.build(),
            "Donacion " + entrega.getDonacionId() + " -> EN_TRASLADO (ruta " + rutaId + ")");
    }

    public boolean notificarEntregaExitosa(Entrega entrega) {
        ActualizacionEstadoRequest.Builder request = ActualizacionEstadoRequest.newBuilder()
            .setDonacionId(entrega.getDonacionId())
            .setNuevoEstado("ENTREGADA");

        LocalDateTime fechaHora = entrega.getFechaHoraEntrega();
        if (fechaHora != null) {
            request.setFechaHora(fechaHora.format(FORMATO_FECHA));
        }

        Camion camion = entrega.getCamionQueEntrego();
        if (camion != null && camion.getPatente() != null) {
            request.setPatenteCamion(camion.getPatente());
        }

        return enviar(request.build(),
            "Donacion " + entrega.getDonacionId() + " -> ENTREGADA");
    }

    public boolean notificarFallaDeEntrega(Long idDonacion, String motivo,
                                           boolean replanificable) {
        return enviar(
            ActualizacionEstadoRequest.newBuilder()
                .setDonacionId(idDonacion)
                .setNuevoEstado("FALLIDA")
                .setMotivo(motivo != null ? motivo : "")
                .setReplanificable(replanificable)
                .build(),
            "Donacion " + idDonacion + " -> FALLIDA (" + motivo + ")");
    }

    private boolean enviar(ActualizacionEstadoRequest request, String descripcion) {
        try {
            ActualizacionEstadoResponse response = stub.actualizarEstadoDonacion(request);

            if (!response.getExito()) {
                System.err.println("[gRPC] Donaciones rechazo el aviso (" + descripcion
                    + "): " + response.getMensaje());
                return false;
            }

            System.out.println("[gRPC] Aviso a Donaciones OK: " + descripcion);
            return true;

        } catch (StatusRuntimeException e) {
            System.err.println("[gRPC] Error de comunicacion (" + descripcion + "): "
                + e.getStatus().getDescription());
            return false;
        } catch (Exception e) {
            System.err.println("[gRPC] Error avisando (" + descripcion + "): "
                + e.getMessage());
            return false;
        }
    }

    private String urlDeSeguimiento(Long rutaId) {
        String base = System.getenv().getOrDefault("CLIENTE_URL", "http://localhost:9000");
        return base + "/donantes/mapa-donaciones?ruta=" + rutaId;
    }
}