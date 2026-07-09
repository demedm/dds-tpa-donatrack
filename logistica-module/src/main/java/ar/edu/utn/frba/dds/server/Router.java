package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.CamionController;
import ar.edu.utn.frba.dds.controllers.RutaController;
import io.javalin.Javalin;

public class Router {
  public void configure(Javalin app) {
    //UsuarioController controller = new UsuarioController();
    CamionController controller = new CamionController();
    RutaController saludo = new RutaController();

    //app.get("/users/random", ctx -> ctx.json(controller.randomUser()));
    app.get("/camiones/random", ctx -> ctx.json(controller.randomCamion()));
    app.get("/camiones/{patente}", ctx -> ctx.json(controller.showCamion(ctx)));

    //app.get("/camiones", ctx -> ctx.render("bienvenida.html.hbs",
    //    controller.index(ctx)));
  }
}