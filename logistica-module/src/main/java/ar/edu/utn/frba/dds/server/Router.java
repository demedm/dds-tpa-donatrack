package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controller.UsuarioController;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

public class Router {
  public void configure(Javalin app) {
    UsuarioController controller = new UsuarioController();

    app.get("/users/random", ctx -> ctx.json(controller.randomUser()));
  }
}