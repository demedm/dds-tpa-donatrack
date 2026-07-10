package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.CamionController;
import ar.edu.utn.frba.dds.controllers.DonacionesController;
import ar.edu.utn.frba.dds.controllers.RutaController;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;
import ar.edu.utn.frba.dds.repositories.DonanteRepository;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.io.IOException;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Router {
  public void configure(Javalin app) throws IOException, InterruptedException {
    DonacionesRepository donacionesRepository = new DonacionesRepository();

    path("/donaciones",() -> {

      app.put("/{id}",
          donacionesRepository::cambiarEstadoDonacion);

      app.get("/", ctx ->
          ctx.json(donacionesRepository.obtenerTodas())
      );

      app.get("/{id})",ctx-> {
        donacionesRepository.obtenerPorId(ctx.pathParam("id"));
        ctx.json(donacionesRepository);

      });

    });

  }

}