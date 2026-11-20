package ar.edu.utn.frba.dds.GrpcServiceImplementation;

import ar.edu.utn.frba.dds.dto.entrada.BienDTO;
import ar.edu.utn.frba.dds.dto.salida.PropuestasSalidaDTO;
import ar.edu.utn.frba.dds.grpc.BienProto;
import ar.edu.utn.frba.dds.grpc.CrearDonacionRequest;
import ar.edu.utn.frba.dds.grpc.DonacionCreadaResponse;
import ar.edu.utn.frba.dds.grpc.DonanteIdRequest;
import ar.edu.utn.frba.dds.grpc.DonanteProto;
import ar.edu.utn.frba.dds.grpc.ListaDonantesResponse;
import ar.edu.utn.frba.dds.grpc.ListaEntidades;
import ar.edu.utn.frba.dds.grpc.ListaSegmentadasResponse;
import ar.edu.utn.frba.dds.grpc.ListarDonantesRequest;
import ar.edu.utn.frba.dds.grpc.RankingResponse;
import ar.edu.utn.frba.dds.grpc.ResultadoOperacion;
import ar.edu.utn.frba.dds.grpc.GestionDonacionesServiceGrpc;
import ar.edu.utn.frba.dds.grpc.SegmentadaIdRequest;
import ar.edu.utn.frba.dds.grpc.SegmentadaProto;
import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.Categoria;
import ar.edu.utn.frba.dds.model.Bienes.EstadoUso;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Bienes.UnidadMedida;
import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.necesidad.Peticion;
import ar.edu.utn.frba.dds.model.notificaciones.EventoNotificacionService;
import ar.edu.utn.frba.dds.repositories.DonanteRepository;
import ar.edu.utn.frba.dds.repositories.EntidadRepository;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;
import ar.edu.utn.frba.dds.grpc.AsignacionRequest;
import ar.edu.utn.frba.dds.dto.EntidadDTO;
import ar.edu.utn.frba.dds.repositories.SubcategoriaRepository;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import ar.edu.utn.frba.dds.grpc.ListarEntidadesRequest;
import ar.edu.utn.frba.dds.grpc.ListaEntidadesResponse;
import ar.edu.utn.frba.dds.grpc.EntidadProto;
import io.grpc.stub.StreamObserver;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class GrpcServiceGestionDonacionesImplementation
    extends GestionDonacionesServiceGrpc.GestionDonacionesServiceImplBase {

    private final DonacionesRepository donacionesRepo = DonacionesRepository.Instance;
    private final EntidadRepository entidadRepo = EntidadRepository.Instance;
    private final EventoNotificacionService notificaciones = EventoNotificacionService.Instance;

    @Override
    public void marcarVencida(SegmentadaIdRequest request, StreamObserver<
            ResultadoOperacion> responseObserver) {

        try{
            DonacionSegmentada segmentada = donacionesRepo.findSegmentadaById(request.getSegmentadaId());

            if(segmentada == null){
                responder(responseObserver, false, "Donacion segmentada " + request.getSegmentadaId() + " no encontrada");
                return;
            }

            donacionesRepo.withTransaction(() ->
                    segmentada.cambiarEstado("VENCIDA"));

            responder(responseObserver, true, "Donación marcada como vencida");

        }catch (Exception e){
            e.printStackTrace();
            responder(responseObserver,false, "No se pudo marcar como vencida: " +
                    e.getMessage());
        }

    }

    @Override
    public void asignarDonacion(AsignacionRequest request, StreamObserver<ResultadoOperacion> responseObserver){
        try {
            DonacionSegmentada segmentada = donacionesRepo.findSegmentadaById(request.getSegmentadaId());

            if(segmentada == null){
                responder(responseObserver, false, "No se encontró la donacion segmentada " + request.getSegmentadaId());
                return;
            }

            if(!segmentada.estaAlmacen()){
                responder(responseObserver,false, "Solo se puede asignar donaciones EN_DEPOSITO (donacion " + request.getSegmentadaId() + ")");
                return;
            }

            EntidadBeneficiaria entidad = entidadRepo.obtenerPorId(request.getEntidadId());
            if(entidad == null){
                responder(responseObserver,false,"No se encontro la entidad " + request.getEntidadId());
                return;
            }

            if (!segmentada.getIdsEntidadesPropuestas().contains(entidad.getId())){
                responder(responseObserver,false,"La entidad " + entidad.getId() + " no está entre las propuestas de la donacion " + segmentada.getId());
                return;
            }

            Peticion peticion = entidad.getNecesidades().stream()
                .flatMap(necesidad -> necesidad.getPeticiones().stream())
                .filter(segmentada::puedeCubrir)
                .findFirst()
                .orElse(null);

            donacionesRepo.withTransaction(() ->
                segmentada.asignarA(entidad.getId(), peticion));

            avisarAsignacion(segmentada,entidad);

            responder(responseObserver, true, "Donacion asignada a " + entidad.nombreParaMostrar());

        }catch (Exception e){
            e.printStackTrace();
            responder(responseObserver,false,"No se pudo asignar la donación: " + e.getMessage());
        } finally {
            WithSimplePersistenceUnit.dispose();
        }

    }

    //pantalla 15 todas las entidades
    @Override
    public void listarEntidades(ListarEntidadesRequest request, StreamObserver<ListaEntidadesResponse> responseObserver){
        try {
            ListaEntidadesResponse.Builder respuesta = ListaEntidadesResponse.newBuilder().setExito(true);

            for (EntidadBeneficiaria entidad : entidadRepo.obtenerEntidades()){
                respuesta.addEntidades(toProto(EntidadDTO.desde(entidad)));
            }

            responseObserver.onNext(respuesta.build());
            responseObserver.onCompleted();
        }catch (Exception e){
            e.printStackTrace();

            responseObserver.onNext(ListaEntidadesResponse.newBuilder()
                .setExito(false)
                .setMensaje("No se pudieron listar las entidades: " + e.getMessage())
                .build());

            responseObserver.onCompleted();
        }finally {
            WithSimplePersistenceUnit.dispose();
        }
    }


    @Override
    public void obtenerRanking(SegmentadaIdRequest request, StreamObserver<RankingResponse> responseObserver){
        try {
            DonacionSegmentada segmentada = donacionesRepo.findSegmentadaById(request.getSegmentadaId());

            if(segmentada == null){
                responseObserver.onNext(RankingResponse.newBuilder()
                    .setExito(false)
                    .setDonacionEncontrada(false)
                    .setMensaje("Donacion segmentada " + request.getSegmentadaId() + " no encontrada")
                    .build());

                responseObserver.onCompleted();
                return;
            }

            if(!segmentada.estaAlmacen()){
                responseObserver.onNext(RankingResponse.newBuilder()
                    .setExito(false)
                    .setDonacionEncontrada(true)
                    .setMensaje("La donacion " + request.getSegmentadaId() + " no se encuentra en deposito ")
                    .build());

                responseObserver.onCompleted();
                return;
            }

            PropuestasSalidaDTO ranking = PropuestasSalidaDTO.desde(segmentada, id -> {
                EntidadBeneficiaria entidad = entidadRepo.obtenerPorId(id);
                return entidad == null ? null :
                    EntidadDTO.desde(entidad);
            });

            RankingResponse.Builder respuesta = RankingResponse.newBuilder()
                .setExito(true)
                .setDonacionEncontrada(true)
                .setHuboCoincidencias(ranking.huboCoincidencias);

            for (EntidadDTO entidad : ranking.propuestas){
                respuesta.addPropuestas(toProto(entidad));
            }

            for(Map.Entry<String, List<EntidadDTO>> algoritmo : ranking.porAlgoritmo.entrySet()){
                ListaEntidades.Builder lista =
                    ListaEntidades.newBuilder();

                for(EntidadDTO entidad : algoritmo.getValue()){
                    lista.addEntidades(toProto(entidad));
                }
                respuesta.putPorAlgoritmo(texto(algoritmo.getKey()), lista.build());
            }

            responseObserver.onNext(respuesta.build());
            responseObserver.onCompleted();

        } catch (Exception e){

            e.printStackTrace();

            responseObserver.onNext(RankingResponse.newBuilder()
                .setExito(false)
                .setDonacionEncontrada(true)
                .setMensaje("No se pudo obtener el ranking: " + e.getMessage())
                .build());

            responseObserver.onCompleted();
        } finally {
            WithSimplePersistenceUnit.dispose();
        }
    }

    @Override
    public void listarDonantes(ListarDonantesRequest request, StreamObserver<ListaDonantesResponse> responseObserver){

        try{
            ListaDonantesResponse.Builder respuesta = ListaDonantesResponse.newBuilder().setExito(true);

            for (Persona donante : DonanteRepository.Instance.getRegistroDonantes()){
                respuesta.addDonantes(DonanteProto.newBuilder()
                    .setId(donante.getId())
                    .setNombreIdentificador(texto(donante.getNombreIdentificador()))
                    .build());
            }

            responseObserver.onNext(respuesta.build());
            responseObserver.onCompleted();
        }catch (Exception e){
            e.printStackTrace();

            responseObserver.onNext(ListaDonantesResponse.newBuilder()
                .setExito(false)
                .setMensaje("No se pudieron listar los donantes: " + e.getMessage())
                .build());

            responseObserver.onCompleted();

        }finally {
            WithSimplePersistenceUnit.dispose();
        }

    }

    //admin registra la donación
    @Override
    public void crearDonacion(CrearDonacionRequest request, StreamObserver<DonacionCreadaResponse> responseObserver){
        try{
            if(request.getDonanteId()== 0){
                responderCreacion(responseObserver, false, "Debe haber un donante", 0);
                return;
            }

            if(request.getBienesCount() == 0){
                responderCreacion(responseObserver,false,"Debe existir al menos un bien",0);
                return;
            }

            Persona donante = DonanteRepository.Instance.buscarPorId(request.getDonanteId());

            if(donante == null){
                responderCreacion(responseObserver,false,"No existe el donante " + request.getDonanteId(),0);
                return;
            }

            List<BienDTO> bienesDTO = new ArrayList<>();
            for (BienProto bien: request.getBienesList()){
                bienesDTO.add(aBienDTO(bien));
            }

            String descripcionGeneral = request.getDescripcionGeneral().isBlank() ? null : request.getDescripcionGeneral();

            Donacion donacion = donacionesRepo.withTransaction(() ->
            {
                List<Bien> bienes = new ArrayList<>();
                for (BienDTO bienDTO: bienesDTO){
                    Subcategoria sub = SubcategoriaRepository.Instance.buscarOCrear(
                        bienDTO.getCategoria(),
                        bienDTO.getSubcategoria(),
                        bienDTO.getUnidad());
                    bienes.add(bienDTO.aBien(sub));
                }

                Donacion nueva = new Donacion(descripcionGeneral,bienes,donante);
                donacionesRepo.guardar(nueva);
                return nueva;
            });

            responderCreacion(responseObserver,true,"Donacion registrada", donacion.getId());


        }catch (IllegalArgumentException e){
            responderCreacion(responseObserver,false,e.getMessage(),0);
        }catch (Exception e){
            e.printStackTrace();
            responderCreacion(responseObserver,false,"No se pudo registrar la donacion: " + e.getMessage(), 0);

        }finally {
            WithSimplePersistenceUnit.dispose();
        }

    }

    @Override
    public void listarDonacionesDeDonante(DonanteIdRequest request, StreamObserver<ListaSegmentadasResponse> responseObserver){
        try {
            ListaSegmentadasResponse.Builder respuesta = ListaSegmentadasResponse.newBuilder().setExito(true);

            Map<Long,String> nombresEntidades = new HashMap<>();

            //efecto de lado
            for (Donacion donacion: donacionesRepo.findDeDonante(request.getDonanteId())){
                for(DonacionSegmentada segmentada : donacion.getSegmentadas()){
                    respuesta.addSegmentadas(aSegmentadaProto(donacion,segmentada,nombresEntidades));
                }
            }
            responseObserver.onNext(respuesta.build());
            responseObserver.onCompleted();
        }catch (Exception e){
            e.printStackTrace();

            responseObserver.onNext(ListaSegmentadasResponse.newBuilder()
                .setExito(false)
                .setMensaje("No se pudieron listar tus donaciones: " + e.getMessage())
                .build());

            responseObserver.onCompleted();
        } finally {
            WithSimplePersistenceUnit.dispose();
        }

    }

    private SegmentadaProto aSegmentadaProto(Donacion donacion, DonacionSegmentada segmentada, Map<Long, String> nombresEntidades){

        Subcategoria sub = segmentada.getSubcategoria();

        SegmentadaProto.Builder proto = SegmentadaProto.newBuilder()
            .setId(segmentada.getId())
            .setDonacionId(donacion.getId())
            .setFechaRecepcion(donacion.getFechaRecepcion() == null ? "" : donacion.getFechaRecepcion().toString())
            .setEstado(texto(segmentada.getEstadoActual().getNombre()))
            .setCategoria(sub.getCategoria().name())
            .setSubcategoria(texto(sub.getDescripcion()))
            .setCantidad(segmentada.getCantidad().toPlainString())
            .setUnidad(sub.getUnidad().name());

        //sin entidad asignada queda "", el texto lo arma el cliente
        Long entidadId = segmentada.getEntidadAsignadaId();

        if(entidadId != null) {
            proto.setEntidadAsignada(nombresEntidades.computeIfAbsent(entidadId, this::nombreEntidad));
        }

        return proto.build();

    }

    private String nombreEntidad(Long entidadId){
        EntidadBeneficiaria entidad = entidadRepo.obtenerPorId(entidadId);

        return entidad == null ? "" : texto(entidad.nombreParaMostrar());
    }

    private BienDTO aBienDTO(BienProto bien){
        if(bien.getSubcategoria().isBlank()){
            throw new IllegalArgumentException("Cada articulo necesita una subcategoria");
        }

        BienDTO dto = new BienDTO();
        dto.setCategoria(aEnum(Categoria.class,bien.getCategoria(),"categoria"));
        dto.setSubcategoria(bien.getSubcategoria());
        dto.setUnidad(aEnum(UnidadMedida.class,bien.getUnidad(),"unidad"));
        dto.setDescripcion(bien.getDescripcion());
        dto.setFoto(bien.getFoto());

        try{
            dto.setCantidad(new BigDecimal(bien.getCantidad()));

        }catch (NumberFormatException e){
            throw new IllegalArgumentException("Cantidad invalida: '" + bien.getCantidad() + "'");
        }

        if(!bien.getFechaVencimiento().isBlank()){
            try{
                dto.setFechaVencimiento(LocalDate.parse(bien.getFechaVencimiento()));
            }catch (DateTimeParseException e){
                throw new IllegalArgumentException("Fecha de vencimiento invalida: '" + bien.getFechaVencimiento() + "'");
            }
        }

        if(!bien.getEstadoUso().isBlank()){
            dto.setEstadoUso(aEnum(EstadoUso.class,bien.getEstadoUso(),"estado de uso"));
        }

        return dto;

    }

    private <E extends Enum<E>> E aEnum(Class<E> tipo, String valor, String campo){

        try{
            return Enum.valueOf(tipo, valor);
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Valor de " + campo + " invalido: '" + valor + "'");
        }
    }

    private void responderCreacion(StreamObserver<DonacionCreadaResponse> responseObserver, boolean exito, String mensaje, long donacionId){
        responseObserver.onNext(DonacionCreadaResponse.newBuilder()
            .setExito(exito)
            .setMensaje(mensaje)
            .setDonacionId(donacionId)
            .build());
        responseObserver.onCompleted();
    }

    private EntidadProto toProto(EntidadDTO entidad){
        EntidadProto.Builder proto = EntidadProto.newBuilder()
            .setRazonSocial(texto(entidad.razonSocial))
            .setTipo(texto(entidad.tipo))
            .setDireccion(texto(entidad.direccion))
            .setTelefono(texto(entidad.telefono));

        if(entidad.id != null){
            proto.setId(entidad.id);
        }
        if (entidad.mails != null){
            proto.addAllMails(entidad.mails);
        }
        return proto.build();
    }

    //grpc no acpeta null en los campos, entonces se manda ""
    private String texto(String valor){
        return valor == null ? "" : valor;
    }

    private void avisarAsignacion(DonacionSegmentada segmentada, EntidadBeneficiaria  entidad){

        String nombreEntidad = entidad.nombreParaMostrar();
        Persona donante = segmentada.getDonante();
        if(donante != null){
            try{
                notificaciones.notificarDonacionAsignadaDonante(donante, segmentada.getId(), nombreEntidad);
            }catch (Exception e){
                System.err.println("[Asignacion] No se pudo avisar al donante de la donacion " + segmentada.getId() + ": " + e.getMessage());
            }
        }

        try {
            notificaciones.notificarDonacionAsignadaBeneficiario(entidad,nombreEntidad, segmentada.getId());
        }catch (Exception e){
            System.err.println("[Asignacion gRPC] No se pudo avisar a la entidad " + nombreEntidad + ": " + e.getMessage());
        }

    }

    private void responder(StreamObserver<ResultadoOperacion> responseObserver, boolean exito, String mensaje){
        responseObserver.onNext(ResultadoOperacion.newBuilder()
                .setExito(exito)
                .setMensaje(mensaje)
                .build());
        responseObserver.onCompleted();
    }
}
