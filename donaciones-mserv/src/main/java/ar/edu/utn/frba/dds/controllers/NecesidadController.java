package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.necesidad.*;
import ar.edu.utn.frba.dds.repositories.*;
import java.time.LocalDate;
import java.util.List;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

public class NecesidadController {

    public Necesidad crear(Context ctx){
        Necesidad necesidad = ctx.bodyValidator(Necesidad.class) //Mapea a una clase
            //valido el body de la necesidad
            .check(n ->n.getEntidadId() !=null, "el id de la entidad es obligatorio")
            // .check(n ->  EntidadRepositorio.findById(n.getEntidadId()), "la entidad ingresada no existe")
            .check(n -> n.getDescripcion() !=null, "una breve descripcion es necesaria")
            .check(n-> {
                if (n.getTipo() == Necesidad.TipoNecesidad.RECURRENTE) {
                        return n.getDiasRecurrencia() != null && n.getDiasRecurrencia() >0;
                }
                return true;
            },"las necesidades recurrentes requieren los dias de recurrencia y mayores a 0")
            .get();
        
        if (necesidad.getTipo() == Necesidad.TipoNecesidad.RECURRENTE) {
            necesidad.setProximoVencimiento(LocalDate.now().plusDays(necesidad.getDiasRecurrencia()));
        }    
        return NecesidadRepository.Instance.crear(necesidad);
    }

    public Necesidad agregarPeticion(Context ctx){
        String necesidadId = ctx.pathParam("id");
        Necesidad necesidad = NecesidadRepository.Instance.findById(necesidadId);
        if (necesidad == null) {
            throw new NotFoundResponse("Necesidad no encontrada");
        }
        Peticion peticion = ctx.bodyValidator(Peticion.class)
        .check(p -> p.getSubclase() !=null,"es necesario agregar la subclase de donacion requerida")
        .check(p -> p.getCantidadRequerida() >0, "la cantidad solicitada es obligatoria y debe ser mayor a 0" )
        .get();

        necesidad.agregarPeticion(peticion);

        return NecesidadRepository.Instance.actualizar(necesidad);
    }

    public Necesidad showNecesidad(Context ctx){
        String necesidadId = ctx.pathParam("id");
        Necesidad necesidad = NecesidadRepository.Instance.findById(necesidadId);
        if(necesidad == null){
            throw new NotFoundResponse("Necesidad no encontrada");
        }
        return NecesidadRepository.Instance.findById(necesidadId);
    }

    public List<Necesidad> showNecesidades(){
        return NecesidadRepository.Instance.findAll();
    }

    public List<Necesidad> showNecesidadesRecurrentes(){
        return NecesidadRepository.Instance.findAllRecurrentes();
    }
}