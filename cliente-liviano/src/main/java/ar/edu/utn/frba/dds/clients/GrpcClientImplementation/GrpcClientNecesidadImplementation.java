package ar.edu.utn.frba.dds.clients.GrpcClientImplementation;

import ar.edu.utn.frba.dds.dto.NecesidadDto;
import ar.edu.utn.frba.dds.dto.PeticionDto;
import ar.edu.utn.frba.dds.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GrpcClientNecesidadImplementation {

    private final NecesidadServiceGrpc.NecesidadServiceBlockingStub stub;
    private final ManagedChannel channel;

    public GrpcClientNecesidadImplementation() {
        this.channel = ManagedChannelBuilder
                .forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        this.stub = NecesidadServiceGrpc.newBlockingStub(channel);
    }

    public Long crearNecesidad(Long entidadId, String descripcion, String tipo, Integer diasRecurrencia) {
        try {
            CrearNecesidadRequest request = CrearNecesidadRequest.newBuilder()
                    .setEntidadId(entidadId)
                    .setDescripcion(descripcion != null ? descripcion : "")
                    .setTipo(tipo != null ? tipo : "NORMAL")
                    .setDiasRecurrencia(diasRecurrencia != null ? diasRecurrencia : 0)
                    .build();

            NecesidadResponse response = stub.crearNecesidad(request);
            if (response.getExito()) {
                return response.getNecesidadId();
            } else {
                System.err.println("Error gRPC crearNecesidad: " + response.getMensaje());
            }
        } catch (Exception e) {
            System.err.println("Fallo conexión gRPC con Donaciones (9090): " + e.getMessage());
        }
        return null;
    }

    public boolean agregarPeticion(Long necesidadId, String subclase, Double cantidadRequerida) {
        try {
            AgregarPeticionRequest request = AgregarPeticionRequest.newBuilder()
                    .setNecesidadId(necesidadId)
                    .setSubclase(subclase != null ? subclase : "")
                    .setCantidadRequerida(cantidadRequerida != null ? cantidadRequerida : 0.0)
                    .build();

            OperacionNecesidadResponse response = stub.agregarPeticion(request);
            return response.getExito();
        } catch (Exception e) {
            System.err.println("Fallo conexión gRPC agregarPeticion: " + e.getMessage());
            return false;
        }
    }

    public List<NecesidadDto> listarPorEntidad(Long entidadId) {
        try {
            ListaNecesidadesResponse response = stub.listarNecesidadesPorEntidad(
                    EntidadIdRequest.newBuilder().setEntidadId(entidadId).build()
            );

            return response.getNecesidadesList().stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Fallo conexión gRPC listarPorEntidad: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public NecesidadDto obtenerDetalle(Long necesidadId) {
        try {
            NecesidadDetailResponse response = stub.obtenerNecesidadDetalle(
                    NecesidadIdRequest.newBuilder().setNecesidadId(necesidadId).build()
            );

            if (response.getEncontrada()) {
                return toDto(response.getNecesidad());
            }
        } catch (Exception e) {
            System.err.println("Fallo conexión gRPC obtenerDetalle: " + e.getMessage());
        }
        return null;
    }
    public boolean agregarAdjunto(Long necesidadId, String url) {
        try {
            AgregarAdjuntoRequest request = AgregarAdjuntoRequest.newBuilder()
                    .setNecesidadId(necesidadId)
                    .setUrl(url)
                    .build();
            OperacionNecesidadResponse response = stub.agregarAdjunto(request);
            if (!response.getExito()) {
                System.err.println("Error gRPC agregarAdjunto: " + response.getMensaje());
            }
            return response.getExito();
        } catch (Exception e) {
            System.err.println("Fallo conexión gRPC agregarAdjunto: " + e.getMessage());
            return false;
        }
    }

    private NecesidadDto toDto(NecesidadProto proto) {
        NecesidadDto dto = new NecesidadDto();
        dto.setId(proto.getId());
        dto.setEntidadId(proto.getEntidadId());
        dto.setTipo(proto.getTipo());
        dto.setEstado(proto.getEstado());
        dto.setDescripcion(proto.getDescripcion());
        dto.setDiasRecurrencia(proto.getDiasRecurrencia() > 0 ? proto.getDiasRecurrencia() : null);
        dto.setProximoVencimiento(proto.getProximoVencimiento());
        dto.setVencida(proto.getEstaVencida());

        List<PeticionDto> peticiones = proto.getPeticionesList().stream().map(p -> {
            PeticionDto pDto = new PeticionDto();
            pDto.setId(p.getId());
            pDto.setSubclase(p.getSubclase());
            pDto.setCantidadRequerida(p.getCantidadRequerida());
            pDto.setCantidadRecibida(p.getCantidadRecibida());
            pDto.setCantidadComprometida(p.getCantidadComprometida());
            return pDto;
        }).collect(Collectors.toList());

        dto.setPeticiones(peticiones);
        dto.setAdjuntos(new java.util.ArrayList<>(proto.getAdjuntosList()));
        return dto;
    }
}