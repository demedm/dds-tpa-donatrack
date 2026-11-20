package ar.edu.utn.frba.dds.clients.GrpcClientImplementation;

import ar.edu.utn.frba.dds.dto.DepositoRequest;
import ar.edu.utn.frba.dds.grpc.ActualizarUbicacionRequest;
import ar.edu.utn.frba.dds.grpc.DepositoResponse;
import ar.edu.utn.frba.dds.grpc.EntregaEnCursoDto;
import ar.edu.utn.frba.dds.grpc.EntregasEnCursoRequest;
import ar.edu.utn.frba.dds.grpc.EntregasEnCursoResponse;
import ar.edu.utn.frba.dds.grpc.GuardarDepositoRequest;
import ar.edu.utn.frba.dds.grpc.IniciarRutaRequest;
import ar.edu.utn.frba.dds.grpc.IniciarRutaResponse;
import ar.edu.utn.frba.dds.grpc.ListarDepositosRequest;
import ar.edu.utn.frba.dds.grpc.ListarDepositosResponse;
import ar.edu.utn.frba.dds.grpc.ObtenerDepositoRequest;
import ar.edu.utn.frba.dds.grpc.PlanificacionServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GrpcClientLogisticaImplementation {

    private final PlanificacionServiceGrpc.PlanificacionServiceBlockingStub stub;
    private static final String TOKEN_SERVICIO = System.getenv().getOrDefault("SERVICE_API_KEY", "");

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

            return responseToList(response.getEntregasList());

        } catch (Exception e) {
            System.err.println("[gRPC Logistica] Error obteniendo entregas en curso: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private List responseToList(List<EntregaEnCursoDto> responseList) {
        return responseList.stream().map(dto -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", dto.getId());
            item.put("donacionId", dto.getDonacionId());
            item.put("direccion", dto.getDireccion());
            item.put("estado", dto.getEstado());
            item.put("latitudCamion", dto.getLatitudCamion());
            item.put("longitudCamion", dto.getLongitudCamion());
            return item;
        }).collect(Collectors.toList());
    }

    public List obtenerTodasLasEntregasEnCurso(String token) {
        try {
            EntregasEnCursoResponse response = stub.obtenerEntregasEnCurso(
                EntregasEnCursoRequest.newBuilder()
                    .setToken(token)
                    .build()
            );
            return responseToList(response.getEntregasList());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Map<String, Object> obtenerDeposito(Long id) {
        try {
            DepositoResponse r = stub.obtenerDeposito(
                ObtenerDepositoRequest.newBuilder().setId(id != null ? id : 0L).build());

            Map<String, Object> out = new HashMap<>();
            out.put("configurado", r.getConfigurado());
            if (r.getConfigurado()) {
                var d = r.getDeposito();
                out.put("id", d.getId());
                out.put("nombre", d.getNombre());
                out.put("direccion", d.getDireccion());
                out.put("latitud", d.getLatitud());
                out.put("longitud", d.getLongitud());
                out.put("principal", d.getPrincipal());
            }
            return out;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Map<String, Object> guardarDeposito(String token, DepositoRequest dto) {
        DepositoResponse r = stub.guardarDeposito(toGrpc(token, dto));

        Map<String, Object> out = new HashMap<>();
        out.put("configurado", r.getConfigurado());
        out.put("id", r.getDeposito().getId());
        return out;
    }

    public static GuardarDepositoRequest toGrpc(String token, DepositoRequest dto) {
        ar.edu.utn.frba.dds.grpc.DepositoDto.Builder deposito =
            ar.edu.utn.frba.dds.grpc.DepositoDto.newBuilder()
            .setNombre(dto.getNombre() != null ? dto.getNombre() : "")
            .setDireccion(dto.getDireccion() != null ? dto.getDireccion() : "")
            .setLatitud(dto.getLatitud() != null ? dto.getLatitud() : 0.0)
            .setLongitud(dto.getLongitud() != null ? dto.getLongitud() : 0.0)
            .setPrincipal(dto.isEsPrincipal());

        return GuardarDepositoRequest.newBuilder()
            .setToken(token != null ? token : "")
            .setDeposito(deposito)
            .build();
    }

    public List<Map<String, Object>> listarDepositos() {
        try {
            ListarDepositosResponse response = stub.listarDepositos(
                ListarDepositosRequest.newBuilder()
                    .build()
            );

            return response.getDepositosList().stream().map(d -> {
                Map<String, Object> item = new HashMap<>();
                item.put("id", d.getId());
                item.put("nombre", d.getNombre());
                item.put("direccion", d.getDireccion());
                item.put("latitud", d.getLatitud());
                item.put("longitud", d.getLongitud());
                item.put("principal", d.getPrincipal());
                return item;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Status.Code actualizarUbicacion(String token, String patente, double lat, double lng) {
        try {
            stub.actualizarUbicacion(ActualizarUbicacionRequest.newBuilder()
                .setToken(TOKEN_SERVICIO)
                .setPatente(patente)
                .setLatitud(lat)
                .setLongitud(lng)
                .build());
            return Status.Code.OK;
        } catch (StatusRuntimeException e) {
            System.err.println(e.getStatus());
            return e.getStatus().getCode();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return Status.Code.UNAVAILABLE;
        }
    }

    public Status.Code iniciarRuta(String patente) {
        try {
            IniciarRutaRequest request = IniciarRutaRequest.newBuilder()
                .setPatente(patente)
                .build();

            IniciarRutaResponse response = stub.iniciarRuta(request);

            if (response.getExito()) {
                return Status.Code.OK;
            }

            System.out.println("No se pudo iniciar la ruta: " + response.getMensaje());
            return Status.Code.FAILED_PRECONDITION;

        } catch (StatusRuntimeException e) {
            System.err.println("Error gRPC al iniciar ruta: " + e.getStatus());
            return e.getStatus().getCode();
        } catch (Exception e) {
            e.printStackTrace();
            return Status.Code.INTERNAL;
        }
    }
}