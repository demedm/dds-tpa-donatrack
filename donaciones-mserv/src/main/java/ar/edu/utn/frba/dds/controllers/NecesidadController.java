package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.necesidad.*;
import ar.edu.utn.frba.dds.repositories.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.UploadedFile;


public class NecesidadController implements WithSimplePersistenceUnit{

    public void crear(Context ctx) {
        Necesidad necesidad;
        String accept = ctx.header("Accept");

        if (accept !=null && accept.contains("text/html")) {
            necesidad = new Necesidad();
            String rawEntidadId = ctx.formParam("entidadId");
            Long entidadId = Long.parseLong(rawEntidadId);
            necesidad.setEntidadId(entidadId);
            necesidad.setDescripcion(ctx.formParam("descripcion"));

            String tipoStr = ctx.formParam("tipo");
            if (tipoStr != null) {
                necesidad.setTipo(Necesidad.TipoNecesidad.valueOf(tipoStr));
            }
            String diasStr = ctx.formParam("diasRecurrencia");
            if (diasStr != null && !diasStr.isBlank()) {
                necesidad.setDiasRecurrencia(Integer.parseInt(diasStr));
            }
        } else {
            necesidad = ctx.bodyValidator(Necesidad.class)
                    .check(n -> n.getEntidadId() != null, "El ID de la entidad es obligatorio")
                    .check(n -> n.getDescripcion() != null, "Una breve descripción es necesaria")
                    .check(n -> {
                        if (n.getTipo() == Necesidad.TipoNecesidad.RECURRENTE) {
                            return n.getDiasRecurrencia() != null && n.getDiasRecurrencia() > 0;
                        }
                        return true;
                    }, "Las necesidades recurrentes requieren días de recurrencia mayores a 0")
                    .get();
        }
        if (necesidad.getTipo() == Necesidad.TipoNecesidad.RECURRENTE && necesidad.getDiasRecurrencia() != null) {
            necesidad.setProximoVencimiento(LocalDate.now().plusDays(necesidad.getDiasRecurrencia()));
        }
        withTransaction(() -> {
            NecesidadRepository.getInstance().guardar(necesidad);
        });
        if (accept !=null && accept.contains("text/html")) {
            ctx.redirect("/necesidades/" + necesidad.getId());
        } else {
            ctx.status(201).json(necesidad);
        }
    }

    public void agregarPeticion(Context ctx){
        Long necesidadId = Long.parseLong(ctx.pathParam("id"));
        Necesidad necesidad = NecesidadRepository.getInstance().findById(necesidadId);
        String accept = ctx.header("Accept");

        if (necesidad == null) {
            throw new NotFoundResponse("Necesidad no encontrada");
        }
        if (accept !=null && accept.contains("text/html")) {
            String subclase = ctx.formParam("subclase");
            String cantidadString =ctx.formParam("cantidadRequerida");
            if (subclase != null && cantidadString != null && !cantidadString.isBlank()) {
                BigDecimal cantidadRequerida;
                try {
                    cantidadRequerida = new BigDecimal(cantidadString.replace(',', '.'));
                } catch (NumberFormatException e) {
                    throw new BadRequestResponse("La cantidad '" + cantidadString + "' no es un número válido");
                }
                if (cantidadRequerida.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new BadRequestResponse("La cantidad solicitada debe ser mayor a 0");
                }
                Peticion nuevaPeticion = new Peticion(subclase, cantidadRequerida);
                necesidad.agregarPeticion(nuevaPeticion);
            }
        } else {
            Peticion peticion = ctx.bodyValidator(Peticion.class)
                    .check(p -> p.getSubclase() !=null,"es necesario agregar la subclase de donacion requerida")
                    .check(p -> p.getCantidadRequerida() != null && p.getCantidadRequerida().compareTo(BigDecimal.ZERO) > 0, "la cantidad solicitada es obligatoria y debe ser mayor a 0" )
                    .get();
            necesidad.agregarPeticion(peticion);
        }

        withTransaction(()->{
            NecesidadRepository.getInstance().actualizar(necesidad);
        });
        if (accept !=null && accept.contains("text/html")) {
            ctx.redirect("/necesidades/" + necesidad.getId());
        } else {
            ctx.status(201).json(necesidad);
        }
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
        // ?entidadId=1 filtra por entidad; sin el parámetro se muestran todas
        Long entidadId = ctx.queryParamAsClass("entidadId", Long.class).allowNullable().get();
        List <Necesidad> necesidades;
        if (entidadId != null){
            necesidades = NecesidadRepository.getInstance().findByEntidadId(entidadId);
        }else{
            necesidades = NecesidadRepository.getInstance().findAll();
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

    public void subirAdjunto(Context ctx) {
        Long necesidadId = Long.parseLong(ctx.pathParam("id"));
        UploadedFile file = ctx.uploadedFile("foto");

        if (file == null) {
            throw new BadRequestResponse("No se seleccionó ningún archivo");
        }
        String nombreArchivo = System.currentTimeMillis() + "_" + file.filename();
        String rutaDestino = "public/uploads/" + nombreArchivo;

        try (InputStream input = file.content()) {
            File targetFile = new File(rutaDestino);
            targetFile.getParentFile().mkdirs();
            Files.copy(input, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen en disco", e);
        }
        String rutaWeb = "/uploads/" + nombreArchivo;

        withTransaction(() -> {
            Necesidad necesidad = NecesidadRepository.getInstance().findById(necesidadId);
            if (necesidad != null) {
                necesidad.agregarAdjunto(rutaWeb);
                NecesidadRepository.getInstance().actualizar(necesidad);
            }
        });
        ctx.redirect("/necesidades/" + necesidadId);
    }
}