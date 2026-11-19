package ar.edu.utn.frba.dds.clients.GrpcClientImplementation;

import ar.edu.utn.frba.dds.dto.EntidadDTO;
import ar.edu.utn.frba.dds.dto.RankingDTO;
import ar.edu.utn.frba.dds.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GrpcClientGestionDonacionesImplementation  {
  private final GestionDonacionesServiceGrpc.GestionDonacionesServiceBlockingStub stub;

  private final ManagedChannel channel;

  public GrpcClientGestionDonacionesImplementation() {
    this.channel = ManagedChannelBuilder
        .forAddress("localhost", 9090)
        .usePlaintext()
        .build();

    this.stub = GestionDonacionesServiceGrpc.newBlockingStub(channel);
  }

  //pantalla 14
  public boolean marcarVencida(Long segmentadaId){
    try{
      ResultadoOperacion respuesta = stub.marcarVencida(
          SegmentadaIdRequest.newBuilder().setSegmentadaId(segmentadaId).build());

      if(!respuesta.getExito()) {
        System.err.println("No vencio la donación " + segmentadaId + ": " + respuesta.getMensaje());
      }
      return respuesta.getExito();
    }catch (Exception e){
      System.err.println("Fallo conexion gRPC con donaciones (9090) marcarVencida: " + e.getMessage());
      return false;
    }
  }

  //pantalla 15
  public boolean asignar(Long segmentadaId, Long entidad){
    try{
      ResultadoOperacion respuesta = stub.asignarDonacion(
          AsignacionRequest.newBuilder()
              .setSegmentadaId(segmentadaId)
              .setEntidadId(entidad)
              .build());

      if(!respuesta.getExito()) {
        System.err.println("No se asignó la donacion " + segmentadaId + ": " + respuesta.getMensaje());
      }

      return respuesta.getExito();
    }catch (Exception e){
      System.err.println("Fallo conexion gRPC con donaciones (9090) asignar: " + e.getMessage());
      return false;
    }
  }

  public List<EntidadDTO> listarEntidades(){
    ListaEntidadesResponse respuesta;

    try{
      respuesta = stub.listarEntidades(ListarEntidadesRequest.newBuilder().build());

    } catch (Exception e){

      throw new RuntimeException("sin conexion gRPC con Donaciones (9090)",e);
    }

    if(!respuesta.getExito()){
      throw new RuntimeException(respuesta.getMensaje());
    }
    return aDtos(respuesta.getEntidadesList());

  }

  //ranking del matchmaking

  public RankingDTO obtenerRanking(Long segmentadaId){
    RankingResponse respuesta;
    try {
      respuesta = stub.obtenerRanking(
          SegmentadaIdRequest.newBuilder().setSegmentadaId(segmentadaId).build());

    }catch (Exception e){
      throw new RuntimeException("sin conexion gRPC con Donaciones (9090)",e);
    }

    if (!respuesta.getDonacionEncontrada()) {
      return null;
    }
    if(!respuesta.getExito()){
      throw new RuntimeException(respuesta.getMensaje());
    }

    Map<String,List<EntidadDTO>> porAlgoritmo = new LinkedHashMap<>();

    for (Map.Entry<String,ListaEntidades> algoritmo : respuesta.getPorAlgoritmoMap().entrySet()){
      porAlgoritmo.put(algoritmo.getKey(), aDtos(algoritmo.getValue().getEntidadesList()));
    }

    RankingDTO rankingDTO = new RankingDTO();
        rankingDTO.setDonacionSegmentadaId(segmentadaId);
        rankingDTO.setHuboCoincidencias(respuesta.getHuboCoincidencias());
        rankingDTO.setPropuestas(aDtos(respuesta.getPropuestasList()));
        rankingDTO.setPorAlgoritmo(porAlgoritmo);
    return rankingDTO;

  }



  private List<EntidadDTO> aDtos(List<EntidadProto> protos){
    List<EntidadDTO> dtos = new ArrayList<>();
    for (EntidadProto p: protos) {
      EntidadDTO dto = new EntidadDTO();
      dto.setId(p.getId());
      dto.setRazonSocial(p.getRazonSocial());
      dto.setTipo(p.getTipo());
      dto.setDireccion(p.getDireccion());
      dto.setMails(new ArrayList<>(p.getMailsList()));
      dto.setTelefono(p.getTelefono());
      dtos.add(dto);
    }
    return dtos;
  }




}
