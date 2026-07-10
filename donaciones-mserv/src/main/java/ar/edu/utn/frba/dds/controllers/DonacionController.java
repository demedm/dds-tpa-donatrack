package ar.edu.utn.frba.dds.controllers;

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
        /*
        DonacionsDTO donacion = ctx.bodyValidator(DonacionsDTO.class)
            .check(d -> d.getDonante() !=null,
                "Debe haber un donante")
            .check(d -> d.getBienes() !=null && !d.getBienes().isEmpty(),
                "Debe existir al menos una donación segementada")
            .get();

         */
        DonacionsDTO donacion = ctx.bodyAsClass(DonacionsDTO.class);

        Donacion d = new Donacion(
            donacion.getDescripcionGeneral(),
            donacion.getBienes(),
            donacion.getDonante()
        );

        DonacionesRepository.Instance.guardar(d);
        return d;
    }

    public Donacion mostrarDonacion(Context ctx){
        String donacionId = ctx.pathParam("id");
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

        String donacionId = ctx.pathParam("id");
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
        String id = ctx.pathParam("id");
        Donacion donacion = DonacionesRepository.Instance.findById(id);

        if(donacion == null){
            throw new NotFoundResponse("Donacion no encontrada");
        }

        DonacionesRepository.Instance.eliminar(id);

        return donacion;
    }


}
