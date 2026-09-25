package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.AsignarDonacionNecesidadDTO;
import ar.edu.utn.frba.dds.dto.DonacionsDTO;
import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.repositories.*;
import ar.edu.utn.frba.dds.model.Estado.*;
import ar.edu.utn.frba.dds.model.necesidad.*;
import java.util.ArrayList;
import java.util.List;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;


public class DonacionController {


    public Donacion crearDonacion(Context ctx) {

        DonacionsDTO donacion = ctx.bodyValidator(DonacionsDTO.class)
            .check(d -> d.getDonante() !=null,
                "Debe haber un donante")
            .check(d -> d.getBienes() !=null && !d.getBienes().isEmpty(),
                "Debe existir al menos una donación segementada")
            .get();

        Donacion d = new Donacion(
            donacion.getDescripcionGeneral(),
            donacion.getBienes(),
            donacion.getDonante()
        );

        DonacionesRepository.Instance.guardar(d);
        return d;
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

                        // Guardar la nueva segmentada en memoria
                        DonacionesRepository.Instance.guardar(donacion);
                        nuevasSegmentadasAsignadas.add(asignada);

                        peticion.setCantidadRecibida(peticion.getCantidadRecibida() + cantidadAAsignar);
                    }
                }
            }

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

            // Responder con la lista de segmentadas generadas por la asignación
            return respuestaDTO;
    }


}
