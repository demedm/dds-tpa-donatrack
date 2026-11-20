package ar.edu.utn.frba.dds.clients.GrpcClientImplementation;

import ar.edu.utn.frba.dds.dto.DonanteDTO;
import ar.edu.utn.frba.dds.dto.EntidadDTO;
import ar.edu.utn.frba.dds.dto.RankingDTO;
import ar.edu.utn.frba.dds.dto.SegmentadaPanelDTO;
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

  //pantalla 13
  public List<DonanteDTO> listarDonantes(){
    ListaDonantesResponse respuesta;

    try{
      respuesta = stub.listarDonantes(ListarDonantesRequest.newBuilder()
          .build());
    }catch (Exception e){
      throw new RuntimeException("sin conexion gRPC con Donaciones (9090)",e);
    }

    if(!respuesta.getExito()){
      throw new RuntimeException(respuesta.getMensaje());
    }

    List<DonanteDTO> donantes = new ArrayList<>();

    for(DonanteProto d : respuesta.getDonantesList()){

      DonanteDTO dto = new DonanteDTO();
      dto.setId(d.getId());
      dto.setNombreIdentificador(d.getNombreIdentificador());
      donantes.add(dto);

    }

    return donantes;

  }

  public Long crearDonacion(Long donanteId, String descripcionGeneral,
                            List<Map<String,String>> articulos){
    CrearDonacionRequest.Builder pedido = CrearDonacionRequest.newBuilder()
        .setDonanteId(donanteId)
        .setDescripcionGeneral(texto(descripcionGeneral));

    for (Map<String,String> articulo : articulos) {
      pedido.addBienes(BienProto.newBuilder()
          .setCategoria(texto(articulo.get("categoria")))
          .setSubcategoria(texto(articulo.get("subcategoria")).toUpperCase())
          .setUnidad(texto(articulo.get("unidad")))
          .setCantidad(texto(articulo.get("cantidad")))
          .setDescripcion(texto(articulo.get("descripcion")))
          .setFoto("")
          .setFechaVencimiento(texto(articulo.get("fechaVencimiento")))
          .setEstadoUso(texto(articulo.get("estadoUso")))
          .build());

    }

    DonacionCreadaResponse respuesta;
    try{
      respuesta = stub.crearDonacion(pedido.build());
    }catch (Exception e){
      throw new RuntimeException("sin conexion gRPC con Donaciones (9090)",e);
    }

    if (!respuesta.getExito()){
      throw new RuntimeException(respuesta.getMensaje());
    }
    return respuesta.getDonacionId();

  }

  //pantalla 5

  public List<SegmentadaPanelDTO> listarDonacionesDeDonante(Long donanteId){

    ListaSegmentadasResponse respuesta;

    try{

      respuesta = stub.listarDonacionesDeDonante(DonanteIdRequest.newBuilder()
          .setDonanteId(donanteId)
          .build());

    }catch (Exception e){
      throw new RuntimeException("sin conexion gRPC con Donaciones (9090)",e);
    }

    if(!respuesta.getExito()){
      throw new RuntimeException(respuesta.getMensaje());
    }

    return respuesta.getSegmentadasList().stream()
        .map(this::aPanelDTO)
        .toList();

  }

  //pantalla 9
  public List<SegmentadaPanelDTO> listarDonacionesAsignadas(Long entidadId){

    ListaSegmentadasResponse respuesta;

    try{
      respuesta = stub.listarDonacionesAsignadas(EntidadAsignadaRequest.newBuilder()
          .setEntidadId(entidadId)
          .build());

    } catch (Exception e){
      throw new RuntimeException("sin conexion gRPC con Donaciones (9090)",e);
    }

    if(!respuesta.getExito()){
      throw new RuntimeException(respuesta.getMensaje());
    }

    return respuesta.getSegmentadasList().stream()
        .map(this::aPanelDTO)
        .toList();

  }

  private SegmentadaPanelDTO aPanelDTO(SegmentadaProto proto){
    SegmentadaPanelDTO dto = new SegmentadaPanelDTO();

    dto.setId(proto.getId());
    dto.setDonacionId(proto.getDonacionId());
    dto.setFechaRecepcion(proto.getFechaRecepcion());
    dto.setEstado(proto.getEstado());
    dto.setCategoria(proto.getCategoria());
    dto.setSubcategoria(proto.getSubcategoria());
    dto.setCantidad(proto.getCantidad());
    dto.setUnidad(proto.getUnidad());
    dto.setEntidadAsignada(proto.getEntidadAsignada());
    return dto;

  }



  private String texto(String valor){
    return valor == null ? "" : valor.trim();
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

  public boolean asignarDonacionManualmente(Long segmentadaId, Long entidadId) {
    try {
      AsignacionRequest request = AsignacionRequest.newBuilder()
          .setSegmentadaId(segmentadaId)
          .setEntidadId(entidadId)
          .build();

      ResultadoOperacion response = stub.asignarDonacionManualmente(request);

      if (!response.getExito()) {
        System.err.println("Error en asignación manual: " + response.getMensaje());
      }

      return response.getExito();

    } catch (Exception e) {
      System.err.println("Error al realizar asignación manual: " + e.getMessage());
      return false;
    }
  }

}
