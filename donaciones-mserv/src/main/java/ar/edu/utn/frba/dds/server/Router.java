package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.DonacionesController;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;
import ar.edu.utn.frba.dds.repositories.DonanteRepository;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.io.IOException;

public class Router {
  public void configure(Javalin app) throws IOException, InterruptedException {
    DonacionesRepository donacionesRepository = new DonacionesRepository();

    app.put("/donaciones/{id}", donacionesRepository::cambiarEstadoDonacion);
  }
}