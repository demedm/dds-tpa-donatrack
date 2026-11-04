package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.necesidad.*;
import ar.edu.utn.frba.dds.repositories.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.apache.commons.collections.map.HashedMap;


public class NecesidadController implements WithSimplePersistenceUnit{

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
        withTransaction(()->{
            NecesidadRepository.getInstance().guardar(necesidad);
        });
        return necesidad;
    }

    public Necesidad agregarPeticion(Context ctx){
        Long necesidadId = Long.parseLong(ctx.pathParam("id"));
        Necesidad necesidad = NecesidadRepository.getInstance().findById(necesidadId);
        if (necesidad == null) {
            throw new NotFoundResponse("Necesidad no encontrada");
        }
        Peticion peticion = ctx.bodyValidator(Peticion.class)
            .check(p -> p.getSubclase() !=null,"es necesario agregar la subclase de donacion requerida")
            .check(p -> p.getCantidadRequerida() >0, "la cantidad solicitada es obligatoria y debe ser mayor a 0" )
            .get();

        necesidad.agregarPeticion(peticion);

        withTransaction(()->{
            NecesidadRepository.getInstance().actualizar(necesidad);
        });
        return necesidad;
    }

    public void showNecesidad(Context ctx){
        String rawId = ctx.pathParam("id");
        Long necesidadId = Long.parseLong(rawId);
        Necesidad necesidad = NecesidadRepository.getInstance().findById(necesidadId);
        if(necesidad == null){
            throw new NotFoundResponse("Necesidad no encontrada");
        }
        String accept =ctx.header("Accept");
        if (accept !=null && accept.contains("text/html")){
            Map<String, Object> model = new HashMap<>();
            model.put("necesidad",necesidad);

            ctx.render("necesidadDetalle.hbs",model);
        }else{
            ctx.json(necesidad);
        }

    }

    public void actualizarVencidas() {
        try {
            withTransaction(()-> {
                List<Necesidad> necesidades = NecesidadRepository.getInstance().findAllRecurrentesActivasVencidas();
                for (Necesidad n : necesidades) {
                    n.setEstado("vencida");
                    NecesidadRepository.getInstance().actualizar(n);
                    Necesidad nuevaRecurrencia = n.crearSiguienteRecurrencia();
                    NecesidadRepository.getInstance().guardar(nuevaRecurrencia);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showNecesidades(Context ctx){
        String entidadId = ctx.queryParam("entidadId");
        List <Necesidad> necesidades;
        if (entidadId !=null && !entidadId.isBlank()){
            necesidades = NecesidadRepository.getInstance().findByEntidadId(entidadId);
        }else{ //GUARDO PARA MOCK
            necesidades =  NecesidadRepository.getInstance().findByEntidadId("ent-001");
        }
        String accept =ctx.header("Accept");
        if (accept !=null && accept.contains("text/html")){
            Map<String, Object> model = new HashMap<>();
            model.put("necesidades",necesidades);
            model.put("entidadId",entidadId);

            ctx.render("necesidades.hbs",model);
        }else{
            ctx.json(necesidades);
        }
    }

    public List<Necesidad> showNecesidadesRecurrentes(){
        return NecesidadRepository.getInstance().findAllRecurrentes();
    }
}