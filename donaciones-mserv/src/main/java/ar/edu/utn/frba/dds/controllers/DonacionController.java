package ar.edu.utn.frba.dds.controllers;

//import ar.edu.utn.frba.dds.dto.AsignarDonacionNecesidadDTO;
import ar.edu.utn.frba.dds.dto.entrada.BienDTO;
import ar.edu.utn.frba.dds.dto.entrada.DonacionsDTO;
import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.repositories.*;

import java.util.ArrayList;
import java.util.List;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;


public class DonacionController {

    public Donacion crearDonacion(Context ctx) {

        DonacionsDTO dto = ctx.bodyValidator(DonacionsDTO.class)
            .check(d -> d.getDonanteId() !=null,
                "Debe haber un donante")
            .check(d -> d.getBienes() !=null && !d.getBienes().isEmpty(),
                "Debe existir al menos una donación segementada")
            .get();

        //el donanten tiene que existir
        Persona donante = DonanteRepository.Instance.buscarPorId(dto.getDonanteId());
        if (donante == null) {
            throw new NotFoundResponse("No existe el donante" + dto.getDonanteId());
        }

        //BienDTO pasa a bien con su subcategoria
        List<Bien> bienes = new ArrayList<Bien>();
        for(BienDTO bienDTO : dto.getBienes()) {
            Subcategoria sub = SubcategoriaRepository.Instance.buscarOCrear(
                bienDTO.getCategoria(), bienDTO.getSubcategoria(),
                bienDTO.getUnidad());

            bienes.add(bienDTO.aBien(sub));
        }

        //la donacion se segmenta sola al crearse
        Donacion donacion = new Donacion(dto.getDescripcionGeneral(), bienes, donante);
        DonacionesRepository.Instance.guardar(donacion);

        return donacion;
    }

    public Donacion mostrarDonacion(Context ctx){
        Long donacionId = ctx.pathParamAsClass("id",Long.class).get();
        Donacion donacion = DonacionesRepository.Instance.findById(donacionId);

        if(donacion == null){
            throw new NotFoundResponse("Donacion no encontrada");
        }
        return donacion;
    }

    public List<Donacion> mostrarDonaciones(){

        return  DonacionesRepository.Instance.obtenerTodas();

    }

    public Donacion actualizar(Context ctx){

        Long donacionId = ctx.pathParamAsClass("id",Long.class).get();
        Donacion donacion = DonacionesRepository.Instance.findById(donacionId);

        if(donacion == null){
            throw new IllegalArgumentException("Donacion no encontrada");
        }

        DonacionsDTO donacionDTO = ctx.bodyAsClass(DonacionsDTO.class);

        if(donacionDTO.getDescripcionGeneral() != null){
            donacion.setDescripcionGeneral(donacionDTO.getDescripcionGeneral());
        }

        DonacionesRepository.Instance.guardar(donacion);

        return donacion;
    }

    public Donacion eliminar(Context ctx){
        Long id = ctx.pathParamAsClass("id",Long.class).get();
        Donacion donacion = DonacionesRepository.Instance.findById(id);

        if(donacion == null){
            throw new NotFoundResponse("Donacion no encontrada");
        }

        DonacionesRepository.Instance.eliminar(id);

        return donacion;
    }

    //PRUEBA ASIGNACION 
/*
    public List<java.util.Map<String, Object>> asignarDonacionANecesidad(Context ctx) {
        AsignarDonacionNecesidadDTO dto = ctx.bodyAsClass(AsignarDonacionNecesidadDTO.class);

        Long idDonacion = dto.getDonacionId();
        Long idNecesidad = dto.getNecesidadId();

        if (idDonacion == null || idNecesidad == null) {
            throw new IllegalArgumentException("Los IDs de donación y necesidad no pueden ser nulos.");
        }
            Donacion donacion = DonacionesRepository.Instance.findById(idDonacion);
            if (donacion == null) {
                throw new NotFoundResponse("No se encontró la donación.");
            }

            Necesidad necesidad = NecesidadRepository.getInstance().findById(idNecesidad);
            if (necesidad == null) {
                throw new NotFoundResponse("No se encontró la necesidad.");
            }

            List<DonacionSegmentada> nuevasSegmentadasAsignadas = new ArrayList<>();

            EntityManagerHelper.beginTransaction();

            for (DonacionSegmentada segmentada : donacion.getDonaciones()) {

                // Solo procesa las que tengan stock disponible y estén en depósito
                if (!segmentada.estaAlmacen() || segmentada.getCantidad() <= 0) {
                    continue;
                }
                for (Peticion peticion : necesidad.getPeticiones()) {

                    if (segmentada.getCantidad() <= 0) break; // Si nos quedamos sin stock en esta segmentada

                    if (peticion.getSubclase().equalsIgnoreCase(segmentada.getSubcategoria().getDescripcion()) &&
                        peticion.getCantidadRecibida() < peticion.getCantidadRequerida()) {

                        int faltantePeticion = peticion.getCantidadRequerida() - peticion.getCantidadRecibida();
                        int cantidadAAsignar = Math.min(segmentada.getCantidad(), faltantePeticion);

                        segmentada.setCantidad(segmentada.getCantidad() - cantidadAAsignar);

                        //creo la nueva segmentada ASIGNADA

                        DonacionSegmentada asignada = new DonacionSegmentada(cantidadAAsignar,segmentada.getSubcategoria(),segmentada.getBienFiltrado());
                        asignada.setEntidadAsignadaId(necesidad.getEntidadId());
                        //asignada.setEstado(EstadoDonacion.ASIGNADA);
                        asignada.asignar();
                        donacion.agregarDonaciones(asignada);

                        // Guardar la nueva segmentada en memoria
                        nuevasSegmentadasAsignadas.add(asignada);

                        peticion.setCantidadRecibida(peticion.getCantidadRecibida() + cantidadAAsignar);
                    }
                }
            }
        DonacionesRepository.Instance.guardar(donacion);

        EntityManagerHelper.commit();

            List<java.util.Map<String, Object>> respuestaDTO = new ArrayList<>();

        for (DonacionSegmentada ds : nuevasSegmentadasAsignadas) {//Pruebo mapenado, si no me devolvia bucle infito
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", ds.getId());
            map.put("cantidad", ds.getCantidad());
            map.put("subcategoria", ds.getSubcategoria().getDescripcion());
            map.put("entidadAsignadaId", ds.getEntidadAsignadaId());
            respuestaDTO.add(map);
        }
        return respuestaDTO;
    }
 */


}

