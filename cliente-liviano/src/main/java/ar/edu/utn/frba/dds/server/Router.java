package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.AdminController;
import ar.edu.utn.frba.dds.controllers.LogisticaController;
import ar.edu.utn.frba.dds.controllers.DonacionController;
import ar.edu.utn.frba.dds.controllers.HomeController;
import ar.edu.utn.frba.dds.controllers.NecesidadController;
import ar.edu.utn.frba.dds.controllers.RolMiddleware;
import ar.edu.utn.frba.dds.controllers.SessionController;
import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
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

    app.before(this::cargarSesion);

    // rutas publicas
    app.get("/", home::landing);
    app.get("/legal", home::mostrarLegal);

    app.before("/login", this::noAutenticados);
    app.get("/login", session::login);
    app.post("/login", session::create);
    app.before("/registro", this::noAutenticados);
    app.get("/registro", session::mostrarRegistro);
    app.post("/registro", session::registrarUsuario);

    app.get("/logout", session::logout);

    app.before("/donaciones", this::autenticados);
    app.get("/donaciones", donacionController::mostrarDonaciones);
    app.post("/donaciones", donacionController::registrarDonacion);

    app.before("/necesidades/*", new RolMiddleware(Rol.BENEFICIARIO)::validarPermisos);
    app.before("/necesidades/*", this::autenticados);
    app.get("/necesidades", necesidadController::showNecesidades);
    app.post("/necesidades/",necesidadController::crear);
    app.get("/necesidades/{id}", necesidadController::showNecesidad);
    app.post("/necesidades/{id}/peticiones", necesidadController::agregarPeticion);
    app.post("/necesidades/{id}/adjuntos", necesidadController::subirAdjunto);

    app.before("/usuarios/*", this::autenticados);
    app.before("/usuarios/*", new RolMiddleware(Rol.BENEFICIARIO)::validarPermisos);
    app.get("/usuarios/{idUsuario}/entregas-en-curso", logisticaController::entregasEnCurso);

    app.before("/admin/*", session::validarAutenticacion);
    app.before("/admin/*", new RolMiddleware(Rol.ADMIN)::validarPermisos);
    app.get("/admin/panel", adminController::administracion);
    app.get("/admin/camiones", logisticaController::mostrarCamiones);
    app.get("/admin/donaciones", donacionController::mostrarDonaciones);
    app.post("/admin/camiones", logisticaController::registrarCamion);
    app.post("/admin/donaciones", donacionController::registrarDonacion);
    app.get("/admin/registro", session::mostrarRegistroAdmin);
    app.post("/admin/registro", session::registrarUsuario);
    app.post("/admin/donantes/importar", adminController::importarDonantesDesdeCsv);

  }

  private void cargarSesion(Context ctx) {
    String token = ctx.cookie("session");
    if (token == null || token.isBlank()) {
      return;
    }
    try {
      ctx.attribute("usuarioLogueado", JwtRepositorio.Instance.obtenerUsuario(token));
    } catch (Exception e) {
      // vencido, firma inválida o rol desconocido: se borra la cookie y se sigue como visitante
      System.err.println("Sesión descartada: " + e.getMessage());
      ctx.header("Set-Cookie", "session=; Path=/; HttpOnly; SameSite=Lax; Secure");
    }
  }

  private void noAutenticados(Context ctx) {
    if (ctx.attribute("usuarioLogueado") != null) {
      ctx.redirect("/");
    }
  }

  private void autenticados(Context ctx) {
    if (ctx.attribute("usuarioLogueado") == null) {
      ctx.redirect("/login");
    }
  }
}
