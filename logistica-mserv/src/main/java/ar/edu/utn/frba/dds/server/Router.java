package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.CamionController;
import ar.edu.utn.frba.dds.controllers.DashboardController;
import ar.edu.utn.frba.dds.controllers.RutaController;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class Router {
  public void configure(Javalin app) {
    CamionController camionController = new CamionController();
    RutaController rutaController = new RutaController();
    DashboardController dashboardController = new DashboardController();

    app.get("/dashboard", ctx -> ctx.render("dashboard.hbs"));
    // app.get("/callback", ctx -> ctx.json(rutaController.saveRutas()));

    app.get("/camiones/random", ctx -> ctx.json(camionController.randomCamion()));
    app.get("/camiones/", ctx -> ctx.json(camionController.showFlota()));
    app.get("/camiones/{patente}", ctx -> ctx.json(camionController.showCamion(ctx)));
    app.get("/camiones/{patente}/entregas/", ctx ->
        ctx.json(camionController.showEntregas(ctx)));
    app.get("/rutas/", ctx -> ctx.json(rutaController.showAllRutas()));
    app.get("/rutas/{id}", ctx -> ctx.json(rutaController.showRuta(ctx)));


    // todavia no implemento el post de donaciones, pero se crean recibiendo el id
    // app.post("/camiones/", ctx -> ctx.json(camionController.save()));
    // app.post("/entregas/");
    //app.get("/camiones", ctx -> ctx.render("dashboard.hbs",
    //    camionController.index(ctx)));
  }
}