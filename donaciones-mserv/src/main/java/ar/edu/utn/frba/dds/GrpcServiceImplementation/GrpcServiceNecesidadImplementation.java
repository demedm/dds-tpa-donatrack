package ar.edu.utn.frba.dds.GrpcServiceImplementation;

import ar.edu.utn.frba.dds.grpc.*;
import ar.edu.utn.frba.dds.model.necesidad.Necesidad;
import ar.edu.utn.frba.dds.model.necesidad.Necesidad.TipoNecesidad;
import ar.edu.utn.frba.dds.model.necesidad.Peticion;
import ar.edu.utn.frba.dds.repositories.NecesidadRepository;
import io.grpc.stub.StreamObserver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GrpcServiceNecesidadImplementation extends NecesidadServiceGrpc.NecesidadServiceImplBase {

    private final NecesidadRepository necesidadRepo = NecesidadRepository.getInstance();

    @Override
    public void crearNecesidad(CrearNecesidadRequest request, StreamObserver<NecesidadResponse> responseObserver) {
        try {
            TipoNecesidad tipo = TipoNecesidad.valueOf(request.getTipo().toUpperCase());
            Integer dias = request.getDiasRecurrencia() > 0 ? request.getDiasRecurrencia() : null;

            Necesidad nueva = new Necesidad();
            nueva.setEntidadId(request.getEntidadId());
            nueva.setDescripcion(request.getDescripcion());
            nueva.setTipo(tipo);
            nueva.setEstado("en_preparacion");
            nueva.setDiasRecurrencia(dias);
            if (dias != null) {
                nueva.setProximoVencimiento(nueva.reiniciarPeriodo());
            }
            nueva.setPeticiones(new ArrayList<>());

            necesidadRepo.withTransaction(() -> {
                necesidadRepo.guardar(nueva);
            });

            NecesidadResponse response = NecesidadResponse.newBuilder()
                    .setExito(true)
                    .setMensaje("Necesidad creada con éxito")
                    .setNecesidadId(nueva.getId())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            e.printStackTrace();
            NecesidadResponse response = NecesidadResponse.newBuilder()
                    .setExito(false)
                    .setMensaje("Error al crear necesidad: " + e.getMessage())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void agregarPeticion(AgregarPeticionRequest request, StreamObserver<OperacionNecesidadResponse> responseObserver) {
        try {
            Necesidad necesidad = necesidadRepo.findById(request.getNecesidadId());
            if (necesidad == null) {
                responseObserver.onNext(OperacionNecesidadResponse.newBuilder()
                        .setExito(false)
                        .setMensaje("Necesidad no encontrada")
                        .build());
                responseObserver.onCompleted();
                return;
            }

            Peticion peticion = new Peticion(
                    request.getSubclase(),
                    BigDecimal.valueOf(request.getCantidadRequerida())
            );

            necesidadRepo.withTransaction(() -> {
                necesidad.agregarPeticion(peticion);
                necesidadRepo.actualizar(necesidad);
            });

            responseObserver.onNext(OperacionNecesidadResponse.newBuilder()
                    .setExito(true)
                    .setMensaje("Petición agregada correctamente")
                    .build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onNext(OperacionNecesidadResponse.newBuilder()
                    .setExito(false)
                    .setMensaje("Error al agregar petición: " + e.getMessage())
                    .build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void listarNecesidadesPorEntidad(EntidadIdRequest request, StreamObserver<ListaNecesidadesResponse> responseObserver) {
        try {
            List<Necesidad> entidades = necesidadRepo.findByEntidadId(request.getEntidadId());
            ListaNecesidadesResponse.Builder builder = ListaNecesidadesResponse.newBuilder();

            for (Necesidad n : entidades) {
                builder.addNecesidades(toProto(n));
            }

            responseObserver.onNext(builder.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onNext(ListaNecesidadesResponse.newBuilder().build());
            responseObserver.onCompleted();
        }
    }

    @Override
    public void obtenerNecesidadDetalle(NecesidadIdRequest request, StreamObserver<NecesidadDetailResponse> responseObserver) {
        try {
            Necesidad n = necesidadRepo.findById(request.getNecesidadId());
            if (n == null) {
                responseObserver.onNext(NecesidadDetailResponse.newBuilder().setEncontrada(false).build());
            } else {
                responseObserver.onNext(NecesidadDetailResponse.newBuilder()
                        .setEncontrada(true)
                        .setNecesidad(toProto(n))
                        .build());
            }
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            responseObserver.onNext(NecesidadDetailResponse.newBuilder().setEncontrada(false).build());
            responseObserver.onCompleted();
        }
    }

    private NecesidadProto toProto(Necesidad n) {
        NecesidadProto.Builder proto = NecesidadProto.newBuilder()
                .setId(n.getId() != null ? n.getId() : 0)
                .setEntidadId(n.getEntidadId() != null ? n.getEntidadId() : 0)
                .setTipo(n.getTipo() != null ? n.getTipo().name() : "NORMAL")
                .setEstado(n.getEstado() != null ? n.getEstado() : "")
                .setDescripcion(n.getDescripcion() != null ? n.getDescripcion() : "")
                .setDiasRecurrencia(n.getDiasRecurrencia() != null ? n.getDiasRecurrencia() : 0)
                .setProximoVencimiento(n.getProximoVencimiento() != null ? n.getProximoVencimiento().toString() : "")
                .setEstaVencida(n.getProximoVencimiento() != null && n.estaVencida());


        if (n.getPeticiones() != null) {
            for (Peticion p : n.getPeticiones()) {
                proto.addPeticiones(PeticionProto.newBuilder()
                        .setId(p.getId() != null ? p.getId() : 0)
                        .setSubclase(p.getSubclase() != null ? p.getSubclase() : "")
                        .setCantidadRequerida(p.getCantidadRequerida() != null ? p.getCantidadRequerida().doubleValue() : 0.0)
                        .setCantidadRecibida(p.getCantidadRecibida() != null ? p.getCantidadRecibida().doubleValue() : 0.0)
                        .setCantidadComprometida(p.getCantidadComprometida() != null ? p.getCantidadComprometida().doubleValue() : 0.0)
                        .build());
            }
        }
        if (n.getAdjuntos() != null) {
            proto.addAllAdjuntos(n.getAdjuntos());
        }
        return proto.build();
    }

    @Override
    public void agregarAdjunto(AgregarAdjuntoRequest request,
                               StreamObserver<OperacionNecesidadResponse> responseObserver) {
        OperacionNecesidadResponse response;
        try {
            if (!request.getUrl().startsWith("https://")) {
                throw new IllegalArgumentException("La URL del adjunto debe ser https");
            }
            Necesidad necesidad = necesidadRepo.findById(request.getNecesidadId());
            if (necesidad == null) {
                throw new IllegalArgumentException("Necesidad no encontrada");
            }

            necesidadRepo.withTransaction(() -> {
                necesidad.agregarAdjunto(request.getUrl());
                necesidadRepo.actualizar(necesidad);
            });

            response = OperacionNecesidadResponse.newBuilder()
                    .setExito(true)
                    .setMensaje("Adjunto agregado")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            response = OperacionNecesidadResponse.newBuilder()
                    .setExito(false)
                    .setMensaje("Error al agregar adjunto: " + e.getMessage())
                    .build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}