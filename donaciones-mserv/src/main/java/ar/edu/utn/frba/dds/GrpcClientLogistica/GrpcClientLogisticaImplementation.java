package ar.edu.utn.frba.dds.GrpcClientLogistica;

import ar.edu.utn.frba.dds.dto.autenticacion.RegistroRequest;
import ar.edu.utn.frba.dds.grpc.ConfirmationPlanificacionResponse;
import ar.edu.utn.frba.dds.grpc.PlanificacionServiceGrpc;
import ar.edu.utn.frba.dds.grpc.RegistroUsuarioResponse;
import ar.edu.utn.frba.dds.grpc.RequestPlanificacionDto;
import ar.edu.utn.frba.dds.grpc.SolicitudPlanificacionRequest;
import ar.edu.utn.frba.dds.dto.DonacionSegmentadaDTO;
import ar.edu.utn.frba.dds.grpc.UsuarioRequest;
import ar.edu.utn.frba.dds.grpc.UsuarioServiceGrpc;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.List;

public class GrpcClientLogisticaImplementation {

    private final PlanificacionServiceGrpc.PlanificacionServiceBlockingStub stub;
    private final UsuarioServiceGrpc.UsuarioServiceBlockingStub usuarioStub;
    private final ManagedChannel channel;

    public GrpcClientLogisticaImplementation() {
        this.channel = ManagedChannelBuilder
                .forAddress("localhost", 9091)
                .usePlaintext()
                .build();

        this.stub = PlanificacionServiceGrpc.newBlockingStub(channel);
        this.usuarioStub = UsuarioServiceGrpc.newBlockingStub(channel);
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
    }

    public boolean enviarUsuario(Usuario usuario) {
        UsuarioRequest request = UsuarioRequest.newBuilder()
            .setId(usuario.getId())
            .setEmail(usuario.getEmail())
            .setRol(usuario.getRol().name())
            .build();

        return enviarRequest(request);
    }

    public boolean enviarBeneficiario(
        Usuario usuario,
        RegistroRequest registro
    ) {
        UsuarioRequest request = UsuarioRequest.newBuilder()
            .setId(usuario.getId())
            .setEmail(usuario.getEmail())
            .setRol(usuario.getRol().name())
            .setRazonSocial(registro.getRazonSocial())
            .setDireccion(registro.getDireccion())
            .build();

        return enviarRequest(request);
    }

    private boolean enviarRequest(UsuarioRequest request) {
        try {
            RegistroUsuarioResponse response =
                usuarioStub.registrarUsuario(request);

            System.out.println("Respuesta de Logística: " + response.getMensaje());

            return response.getExito();

        } catch (io.grpc.StatusRuntimeException e) {
            System.err.println("Error gRPC: " + e.getStatus());
            return false;

        } catch (Exception e) {
            System.err.println("Error al enviar usuario: " + e.getMessage());
            return false;
        }
    }

}