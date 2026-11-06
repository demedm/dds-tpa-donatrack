package ar.edu.utn.frba.dds.GrpcClientLogistica;

import ar.edu.utn.frba.dds.grpc.ConfirmationPlanificacionResponse;
import ar.edu.utn.frba.dds.grpc.PlanificacionServiceGrpc;
import ar.edu.utn.frba.dds.grpc.RequestPlanificacionDto;
import ar.edu.utn.frba.dds.grpc.SolicitudPlanificacionRequest;
import ar.edu.utn.frba.dds.dto.DonacionSegmentadaDTO;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.List;

public class GrpcClientLogisticaImplementation {

    private final PlanificacionServiceGrpc.PlanificacionServiceBlockingStub stub;
    private final ManagedChannel channel;

    public GrpcClientLogisticaImplementation() {
        this.channel = ManagedChannelBuilder
                .forAddress("localhost", 9091)
                .usePlaintext()
                .build();

        this.stub = PlanificacionServiceGrpc.newBlockingStub(channel);
    }

    public void enviarDonacionesEnLotes(List<DonacionSegmentadaDTO> donacionesListas) {
        int maximoPorLote = 100; // Restricción del proveedor logístico

        for (int i = 0; i < donacionesListas.size(); i += maximoPorLote) {
            int fin = Math.min(donacionesListas.size(), i + maximoPorLote);
            List<DonacionSegmentadaDTO> lote = donacionesListas.subList(i, fin);

            try {
                List<RequestPlanificacionDto> donacionesGrpc = lote.stream()
                        .map(d -> RequestPlanificacionDto.newBuilder()
                                .setIdDonacion(d.getIdDonacion())
                                .setDireccion(d.getDireccionEntidad())
                                .build())
                        .toList();

                SolicitudPlanificacionRequest request = SolicitudPlanificacionRequest.newBuilder()
                        .addAllDonaciones(donacionesGrpc)
                        .setUrlCallback("/callback/planificaciones/")
                        .build();

                ConfirmationPlanificacionResponse response = stub.solicitarPlanificacion(request);

                System.out.println("✅ Lote de " + lote.size() + " donaciones enviado a Logística. ID Respuesta: " + response.getResponseId());

            } catch (Exception e) {
                System.err.println("❌ Error al enviar lote a logística: " + e.getMessage());
            }
        }
    }}