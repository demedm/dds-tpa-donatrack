package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.DonacionController;
import ar.edu.utn.frba.dds.controllers.NecesidadController;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;
import ar.edu.utn.frba.dds.repositories.DonanteRepository;
import ar.edu.utn.frba.dds.repositories.EntidadRepository;
import ar.edu.utn.frba.dds.repositories.NecesidadRepository;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.io.IOException;

public class Router {
  public void configure(Javalin app) throws IOException, InterruptedException {
    NecesidadController necesidadController = new NecesidadController();
    DonacionController donacionController = new DonacionController();
    DonacionesRepository donacionesRepository = new DonacionesRepository();

    app.put("/donaciones/{id}", donacionesRepository::cambiarEstadoDonacion);

    //Donaciones

    app.patch("/donaciones/{idDonacion}/necesidades/{idNecesidad}", ctx -> ctx.json(donacionController.asignar(ctx)));

    //Necesidades

    app.post("/necesidades/",ctx -> ctx.status(201).json(necesidadController.crear(ctx)));
    app.post("/necesidades/{id}/peticiones/",ctx -> ctx.status(201).json(necesidadController.agregarPeticion(ctx)));

    app.get("/necesidades/recurrentes", ctx -> ctx.json(necesidadController.showNecesidadesRecurrentes()));
    app.get("/necesidades/", ctx -> ctx.status(201).json(necesidadController.showNecesidades()));
    app.get("/necesidades/{id}",ctx -> ctx.json(necesidadController.showNecesidad(ctx)));
  }
}