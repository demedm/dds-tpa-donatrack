package ar.edu.utn.frba.dds.clients.GrpcClientImplementation;

import ar.edu.utn.frba.dds.grpc.EntregasEnCursoRequest;
import ar.edu.utn.frba.dds.grpc.EntregasEnCursoResponse;
import ar.edu.utn.frba.dds.grpc.PlanificacionServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GrpcClientLogisticaImplementation {

    private final PlanificacionServiceGrpc.PlanificacionServiceBlockingStub stub;

    public GrpcClientLogisticaImplementation() {
        String host = System.getenv().getOrDefault("LOGISTICA_GRPC_HOST", "localhost");
        int port = Integer.parseInt(System.getenv().getOrDefault("LOGISTICA_GRPC_PORT", "9091"));

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();

        this.stub = PlanificacionServiceGrpc.newBlockingStub(channel);
    }

    public List obtenerEntregasEnCurso(Long entidadId) {
        try {
            EntregasEnCursoResponse response = stub.obtenerEntregasEnCurso(
                    EntregasEnCursoRequest.newBuilder()
                            .setEntidadId(entidadId)
                            .build()
            );

            return response.getEntregasList().stream().map(dto -> {
                Map<String, Object> item = new HashMap<>();
                item.put("id", dto.getId());
                item.put("donacionId", dto.getDonacionId());
                item.put("direccion", dto.getDireccion());
                item.put("estado", dto.getEstado());
                item.put("latitudCamion", dto.getLatitudCamion());
                item.put("longitudCamion", dto.getLongitudCamion());
                return item;
            }).collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("[gRPC Logistica] Error obteniendo entregas en curso: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}