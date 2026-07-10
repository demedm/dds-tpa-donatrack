package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.DonacionController;
import ar.edu.utn.frba.dds.controllers.DonacionSegmentadaController;
import ar.edu.utn.frba.dds.controllers.DonanteController;
import ar.edu.utn.frba.dds.controllers.EntidadBeneficiariaController;
import ar.edu.utn.frba.dds.controllers.NecesidadController;
import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import io.javalin.Javalin;

import java.io.IOException;

public class Router {
  public void configure(Javalin app) throws IOException, InterruptedException {
    NecesidadController necesidadController = new NecesidadController();
    DonacionController donacionController = new DonacionController();
    DonanteController donanteController = new DonanteController();
    EntidadBeneficiariaController entidadController = new EntidadBeneficiariaController();
    DonacionSegmentadaController donacionSegmentadaController = new DonacionSegmentadaController();

    //Donaciones

    app.get("/donaciones/", ctx ->
        ctx.json(donacionController.mostrarDonaciones())
    );

    app.post("/donaciones",ctx->{
      ctx.status(201).json(donacionController.crearDonacion(ctx));
    });

    app.get("/donaciones/{id}",ctx-> {
      ctx.status(200).json(donacionController.mostrarDonacion(ctx));
    });

    app.delete("/donaciones/{id}",ctx->{
      donacionController.eliminar(ctx);
      ctx.status(200);
    });


    //app.put("/donaciones-segementada/{id}", ctx ->
    //    ctx.status(200).json(donacionSegmentadaController.cambiarEstadoDonacion(ctx));


    //app.patch("/donaciones/{idDonacion}/necesidades/{idNecesidad}", ctx -> ctx.json(donacionController.asignar(ctx)));

/*

    app.post("/donaciones/{id}/matchmaking",ctx->{
      Donacion donacion = repository.obtenerPorId(ctx.pathParam("id"));
      if(donacion == null) ctx.status(400);
    });

 */


    //Necesidades

    app.post("/necesidades/",ctx ->
        ctx.status(201).json(necesidadController.crear(ctx)));
    app.post("/necesidades/{id}/peticiones/",ctx ->
        ctx.status(201).json(necesidadController.agregarPeticion(ctx)));

    app.get("/necesidades/recurrentes", ctx ->
        ctx.json(necesidadController.showNecesidadesRecurrentes()));
    app.get("/necesidades/", ctx ->
        ctx.status(201).json(necesidadController.showNecesidades()));
    app.get("/necesidades/{id}",ctx ->
        ctx.json(necesidadController.showNecesidad(ctx)));

    //Donante
    app.post("/donantes/fisicas", donanteController::crearDonanteFisica);
    app.post("/donantes/juridicas", donanteController::crearDonanteJuridica);
    app.get("/donantes/", ctx -> ctx.json(donanteController.obtenerDonantes(ctx)));
    app.get("/donantes/{email}", ctx -> ctx.json(donanteController.obtenerDonantePorEmail(ctx)));
    app.put("/donantes/{email}", ctx -> ctx.json(donanteController.actualizarDonante(ctx)));
    app.delete("/donantes/{email}", donanteController::eliminarDonante);

    //Entidades beneficiarias
    app.post("/entidades", entidadController::crearEntidad);
    app.get("/entidades/{id}", ctx -> ctx.json(entidadController.obtenerEntidad(ctx)));
    app.put("/entidades/{id}", ctx -> ctx.json(entidadController.actualizarEntidad(ctx)));
    app.delete("/entidades/{id}", entidadController::deleteEntidad);
    app.get("/entidades",ctx -> ctx.json(entidadController.obtenerEntidades()));
  }
}