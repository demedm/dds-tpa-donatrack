package ar.edu.utn.frba.dds.controllers;

//import ar.edu.utn.frba.dds.dto.AsignarDonacionNecesidadDTO;
import ar.edu.utn.frba.dds.dto.entrada.BienDTO;
import ar.edu.utn.frba.dds.dto.entrada.DonacionsDTO;
import ar.edu.utn.frba.dds.dto.salida.DonacionSalidaDTO;
import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.notificaciones.EventoNotificacionService;
import ar.edu.utn.frba.dds.repositories.*;

import java.util.ArrayList;
import java.util.List;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;


public class DonacionController implements WithSimplePersistenceUnit {

    //crear donacion
    public DonacionSalidaDTO crearDonacion(Context ctx) {
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

        Donacion donacion = withTransaction(() -> {
            //BienDTO pasa a bien con su subcategoria
            List<Bien> bienes = new ArrayList<>();

            for(BienDTO bienDTO : dto.getBienes()) {
                Subcategoria sub = SubcategoriaRepository.Instance.buscarOCrear(
                    bienDTO.getCategoria(), bienDTO.getSubcategoria(),
                    bienDTO.getUnidad());

                bienes.add(bienDTO.aBien(sub));
            }

            //la donacion se segmenta sola al crearse
            Donacion nueva = new Donacion(dto.getDescripcionGeneral(), bienes, donante);
            DonacionesRepository.Instance.guardar(nueva);
            return nueva;
        });

        return DonacionSalidaDTO.desde(donacion);
    }

    //mostrar donacion
    public DonacionSalidaDTO mostrarDonacion(Context ctx){
        Long donacionId = ctx.pathParamAsClass("id",Long.class).get();
        Donacion donacion = DonacionesRepository.Instance.findById(donacionId);

        if(donacion == null){
            throw new NotFoundResponse("Donacion no encontrada");
        }
        return DonacionSalidaDTO.desde(donacion);
    }

    //mostra donaciones
    public List<DonacionSalidaDTO> mostrarDonaciones(){

        return  DonacionesRepository.Instance.obtenerTodas().stream()
            .map(DonacionSalidaDTO::desde)
            .toList();
    }

    public DonacionSalidaDTO actualizar(Context ctx){

        Long donacionId = ctx.pathParamAsClass("id",Long.class).get();
        Donacion donacion = DonacionesRepository.Instance.findById(donacionId);

        if(donacion == null){
            throw new NotFoundResponse("Donacion no encontrada");
        }

        DonacionsDTO donacionDTO = ctx.bodyAsClass(DonacionsDTO.class);

        withTransaction(() -> {
            if(donacionDTO.getDescripcionGeneral() != null){
                donacion.setDescripcionGeneral(donacionDTO.getDescripcionGeneral());
            }

            DonacionesRepository.Instance.guardar(donacion);
        });

        return DonacionSalidaDTO.desde(donacion);
    }

    public void eliminar(Context ctx){
        Long id = ctx.pathParamAsClass("id",Long.class).get();
        Donacion donacion = DonacionesRepository.Instance.findById(id);

        if(donacion == null){
            throw new NotFoundResponse("Donacion no encontrada");
        }

        withTransaction( () -> DonacionesRepository.Instance.eliminar(id));
    }

}

