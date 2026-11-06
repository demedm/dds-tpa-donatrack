package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.DonacionController;
import ar.edu.utn.frba.dds.controllers.HomeController;
import ar.edu.utn.frba.dds.controllers.NecesidadController;
import ar.edu.utn.frba.dds.controllers.SessionController;
import io.javalin.Javalin;

public class Router {
  public void configure(Javalin app) {
    var home = new HomeController();
    var session = new SessionController();
    var necesidadController = new NecesidadController();
    var donacionController = new DonacionController();

    // rutas publicas
    app.get("/", home::landing);
    app.get("/legal", home::mostrarLegal);

    app.get("/login", session::login);
    app.post("/login", session::create);
    app.get("/registro", session::mostrarRegistro);
    app.post("/registro", session::registrarUsuario);

    app.get("/donaciones", donacionController::mostrarDonaciones);
    app.post("/donaciones", donacionController::registrarDonacion);

    app.post("/necesidades/",necesidadController::crear);
    app.post("/necesidades/{id}/peticiones/",necesidadController::agregarPeticion);
    app.post("/necesidades/{id}/adjuntos", necesidadController::subirAdjunto);

  }
}
