package ar.edu.utn.frba.dds.GrpcServiceImplementation;

import ar.edu.utn.frba.dds.casosDeUso.CambioEstadoDonacion;
import ar.edu.utn.frba.dds.dto.CambioEstadoDTO;
import com.donacionesServicegrpc.grpc.ActualizacionEstadoRequest;
import com.donacionesServicegrpc.grpc.ActualizacionEstadoResponse;
import com.donacionesServicegrpc.grpc.donacionesServiceGrpc;
import io.grpc.stub.StreamObserver;

public class GrpcServiceDonacionImplementation
    extends donacionesServiceGrpc.donacionesServiceImplBase {

    private final CambioEstadoDonacion cambioEstado;

    public GrpcServiceDonacionImplementation() {
        this(CambioEstadoDonacion.Instance);
    }

    public GrpcServiceDonacionImplementation(CambioEstadoDonacion cambioEstado) {
        this.cambioEstado = cambioEstado;
    }

    @Override
    public void actualizarEstadoDonacion(
        ActualizacionEstadoRequest request,
        StreamObserver<ActualizacionEstadoResponse> responseObserver) {

        ActualizacionEstadoResponse response;

        try {
            cambioEstado.aplicar(request.getDonacionId(), comoCambioEstado(request));

            response = ActualizacionEstadoResponse.newBuilder()
                .setExito(true)
                .setMensaje("Estado actualizado correctamente en Donaciones")
                .build();

        } catch (Exception e) {
            System.err.println("[gRPC] Error actualizando donacion "
                + request.getDonacionId() + ": " + e.getMessage());

            response = ActualizacionEstadoResponse.newBuilder()
                .setExito(false)
                .setMensaje(e.getMessage())
                .build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private CambioEstadoDTO comoCambioEstado(ActualizacionEstadoRequest request) {
        CambioEstadoDTO cambio = new CambioEstadoDTO();

        cambio.setNuevoEstado(request.getNuevoEstado());
        cambio.setReplanificable(request.getReplanificable());

        copiarSiTieneTexto(request.getMotivo(), cambio::setMotivoFalla);
        copiarSiTieneTexto(request.getRutaId(), cambio::setRutaId);
        copiarSiTieneTexto(request.getUrlMapa(), cambio::setUrlMapa);
        copiarSiTieneTexto(request.getFechaHora(), cambio::setFechaHora);
        copiarSiTieneTexto(request.getPatenteCamion(), cambio::setPatenteCamion);

        return cambio;
        }


    private void copiarSiTieneTexto(String valor, java.util.function.Consumer<String> destino) {
        if (valor != null && !valor.isBlank()) {
            destino.accept(valor);
        }
    }
}
