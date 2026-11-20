package ar.edu.utn.frba.dds.grpcServiceImplementation;


import ar.edu.utn.frba.dds.exceptions.DepositoNotFoundException;
import ar.edu.utn.frba.dds.grpc.*;
import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.EstadoEntrega;
import ar.edu.utn.frba.dds.model.monitoreo.Deposito;
import ar.edu.utn.frba.dds.model.monitoreo.Ubicacion;
import ar.edu.utn.frba.dds.repositories.EntregaRepositorio;
import ar.edu.utn.frba.dds.repositories.autenticacion.JwtRepositorio;
import ar.edu.utn.frba.dds.repositories.autenticacion.Rol;
import ar.edu.utn.frba.dds.repositories.monitoreo.MonitoreoRepositorio;
import ar.edu.utn.frba.dds.scripts.dto.autenticacion.UsuarioLogueado;
import io.grpc.Status;
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
        List<Entrega> entregas = List.of();
        if (!request.getToken().isBlank()) {
            UsuarioLogueado usuario = JwtRepositorio.Instance.obtenerUsuario(request.getToken());
            if (usuario.getRol() != Rol.ADMIN) {
                responseObserver.onError(Status.PERMISSION_DENIED
                    .withDescription("Usuario no tiene permisos suficientes.")
                    .asRuntimeException()
                );
                return;
            }
            entregas = EntregaRepositorio.Instance.mostrarTodos().stream().filter(entrega ->
                entrega.getEstado() == EstadoEntrega.EN_TRASLADO).toList();
        } else {
            entregas = EntregaRepositorio.Instance
                .buscarEnTrasladoDeEntidad(request.getEntidadId());
        }

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

    @Override
    public void obtenerDeposito(ObtenerDepositoRequest request,
                                StreamObserver<DepositoResponse> responseObserver) {
        try {
            MonitoreoRepositorio.Instance.entityManager().clear();

            Deposito deposito = request.getId() > 0
                ? MonitoreoRepositorio.Instance.buscarXid(request.getId())
                : MonitoreoRepositorio.Instance.buscarPrincipal();

            DepositoResponse.Builder response = DepositoResponse.newBuilder();
            if (deposito == null) {
                response.setConfigurado(false);
            } else {
                response.setConfigurado(true).setDeposito(toDto(deposito));
            }

            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
        } catch (DepositoNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND
                .withDescription("No existe un depósito con id " + request.getId())
                .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                .withDescription("Error al obtener el depósito: " + e.getMessage())
                .withCause(e)
                .asRuntimeException());
        }
    }

    @Override
    public void guardarDeposito(GuardarDepositoRequest request,
                                StreamObserver<DepositoResponse> responseObserver) {
        if (request.getToken().isBlank()) {
            responseObserver.onError(Status.UNAUTHENTICATED
                .withDescription("Se requiere un token.")
                .asRuntimeException());
            return;
        }

        try {
            UsuarioLogueado usuario = JwtRepositorio.Instance.obtenerUsuario(request.getToken());
            if (usuario.getRol() != Rol.ADMIN) {
                responseObserver.onError(Status.PERMISSION_DENIED
                    .withDescription("Usuario no tiene permisos suficientes.")
                    .asRuntimeException());
                return;
            }
        } catch (RuntimeException e) {
            responseObserver.onError(Status.UNAUTHENTICATED
                .withDescription("Token inválido o expirado.")
                .asRuntimeException());
            return;
        }

        if (!request.hasDeposito()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                .withDescription("Falta el depósito.")
                .asRuntimeException());
            return;
        }

        DepositoDto depositoDto = request.getDeposito();

        if (depositoDto.getNombre().isBlank() || depositoDto.getDireccion().isBlank()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                .withDescription("Nombre y dirección son obligatorios.")
                .asRuntimeException());
            return;
        }
        if (depositoDto.getLatitud() < -90 || depositoDto.getLatitud() > 90
            || depositoDto.getLongitud() < -180 || depositoDto.getLongitud() > 180) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                .withDescription("Coordenadas fuera de rango.")
                .asRuntimeException());
            return;
        }
        if (depositoDto.getId() < 0) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                .withDescription("Id inválido.")
                .asRuntimeException());
            return;
        }

        try {
            MonitoreoRepositorio.Instance.entityManager().clear();

            Deposito deposito;
            if (depositoDto.getId() > 0) {
                deposito = MonitoreoRepositorio.Instance.buscarXid(depositoDto.getId());
            } else if (depositoDto.getPrincipal()) {
                Deposito principal = MonitoreoRepositorio.Instance.buscarPrincipal();
                deposito = principal != null ? principal : new Deposito();
            } else {
                deposito = new Deposito();
            }                                              // alta

            deposito.setNombre(depositoDto.getNombre());
            deposito.setDireccion(depositoDto.getDireccion());
            deposito.setLatitud(depositoDto.getLatitud());
            deposito.setLongitud(depositoDto.getLongitud());
            deposito.setEsPrincipal(depositoDto.getPrincipal());

            Deposito guardado = MonitoreoRepositorio.Instance.guardar(deposito);

            responseObserver.onNext(DepositoResponse.newBuilder()
                .setConfigurado(true)
                .setDeposito(toDto(guardado))
                .build());
            responseObserver.onCompleted();
        } catch (DepositoNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND
                .withDescription("No existe un depósito con id " + depositoDto.getId())
                .asRuntimeException());
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                .withDescription("Error al guardar el depósito: " + e.getMessage())
                .withCause(e)
                .asRuntimeException());
        }
    }

    @Override
    public void listarDepositos(ListarDepositosRequest request,
                                StreamObserver<ListarDepositosResponse> responseObserver) {
        try {
            MonitoreoRepositorio.Instance.entityManager().clear();

            ListarDepositosResponse.Builder response = ListarDepositosResponse.newBuilder();
            MonitoreoRepositorio.Instance.mostrarTodosDepositos()
                .forEach(deposito -> response.addDepositos(toDto(deposito)));

            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                .withDescription("Error al listar depósitos: " + e.getMessage())
                .withCause(e)
                .asRuntimeException());
        }
    }

    private DepositoDto toDto(Deposito deposito) {
        return DepositoDto.newBuilder()
            .setId(deposito.getId() != null ? deposito.getId() : 0L)
            .setNombre(deposito.getNombre() != null ? deposito.getNombre() : "")
            .setDireccion(deposito.getDireccion() != null ? deposito.getDireccion() : "")
            .setLatitud(deposito.getLatitud() != null ? deposito.getLatitud() : 0.0)
            .setLongitud(deposito.getLongitud() != null ? deposito.getLongitud() : 0.0)
            .setPrincipal(deposito.isPrincipal())
            .build();
    }

}