package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.AdminController;
import ar.edu.utn.frba.dds.controllers.LogisticaController;
import ar.edu.utn.frba.dds.controllers.DonacionController;
import ar.edu.utn.frba.dds.controllers.HomeController;
import ar.edu.utn.frba.dds.controllers.NecesidadController;
import ar.edu.utn.frba.dds.controllers.RolMiddleware;
import ar.edu.utn.frba.dds.controllers.SessionController;
import ar.edu.utn.frba.dds.repositorios.JwtRepositorio;
import ar.edu.utn.frba.dds.session.Rol;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class Router {
  public void configure(Javalin app) {
    var home = new HomeController();
    var session = new SessionController();
    var necesidadController = new NecesidadController();
    var donacionController = new DonacionController();
    var logisticaController = new LogisticaController();
    var adminController = new AdminController();

    // rutas publicas
    app.get("/", home::landing);
    app.get("/legal", home::mostrarLegal);

    app.before("/login", ctx -> redireccionar(ctx, "/login"));
    app.get("/login", session::login);
    app.post("/login", session::create);
    app.before("/registro", ctx -> redireccionar(ctx, "/registro"));
    app.get("/registro", session::mostrarRegistro);
    app.post("/registro", session::registrarUsuario);

    app.before("/donaciones", ctx -> redireccionar(ctx, "/donaciones"));
    app.get("/donaciones", donacionController::mostrarDonaciones);
    app.post("/donaciones", donacionController::registrarDonacion);

    app.before("/necesidades/*", new RolMiddleware(Rol.BENEFICIARIO)::validarPermisos);
    app.post("/necesidades/",necesidadController::crear);
    app.post("/necesidades/{id}/peticiones/",necesidadController::agregarPeticion);
    app.post("/necesidades/{id}/adjuntos", necesidadController::subirAdjunto);

    app.before("/admin/*", session::validarAutenticacion);
    app.before("/admin/*", new RolMiddleware(Rol.ADMIN)::validarPermisos);
    app.get("/admin/camiones", logisticaController::mostrarCamiones);
    app.get("/admin/donaciones", donacionController::mostrarDonaciones);
    app.post("/admin/donaciones", donacionController::registrarDonacion);
    app.post("/admin/donantes", adminController::registrarDonante);

  }

  private void redireccionar(Context ctx, String path) {
    String token = ctx.cookie("session");
    if (token == null || token.isBlank()) {
      return;
    }
    try {
      JwtRepositorio.Instance.obtenerUsuario(token);
      ctx.redirect("/");
    } catch (Exception e) {
      ctx.redirect(path);
    }
  }
}
