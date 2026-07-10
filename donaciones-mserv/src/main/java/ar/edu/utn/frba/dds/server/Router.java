package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.DonacionesController;
import ar.edu.utn.frba.dds.controllers.DonanteController;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;
import ar.edu.utn.frba.dds.repositories.DonanteRepository;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.io.IOException;

public class Router {
  public void configure(Javalin app) throws IOException, InterruptedException {
    DonacionesRepository donacionesRepository = new DonacionesRepository();
    DonanteController donanteController = new DonanteController();
    app.put("/donaciones/{id}", donacionesRepository::cambiarEstadoDonacion);

    app.post("/donantes/", donanteController:: crearDonante);
    app.get("/donantes/", ctx -> ctx.json(donanteController.obtenerDonantes(ctx)));
    app.get("/donantes/{email}", ctx -> ctx.json(donanteController.obtenerDonantePorEmail(ctx)));
    app.put("/donantes/{email}", ctx -> ctx.json(donanteController.actualizarDonante(ctx)));
    app.delete("/donantes/{email}", donanteController::eliminarDonante);
  }
}