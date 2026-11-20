package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.AdminController;
import ar.edu.utn.frba.dds.controllers.DonanteController;
import ar.edu.utn.frba.dds.controllers.LogisticaController;
import ar.edu.utn.frba.dds.controllers.DonacionController;
import ar.edu.utn.frba.dds.controllers.HomeController;
import ar.edu.utn.frba.dds.controllers.NecesidadController;
import ar.edu.utn.frba.dds.controllers.RolMiddleware;
import ar.edu.utn.frba.dds.controllers.SessionController;
import ar.edu.utn.frba.dds.controllers.TelemetriaController;
import ar.edu.utn.frba.dds.repositorios.JwtRepositorio;
import ar.edu.utn.frba.dds.session.Rol;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.RedirectResponse;

public class Router {
  public void configure(Javalin app) {
    var home = new HomeController();
    var session = new SessionController();
    var necesidadController = new NecesidadController();
    var donacionController = new DonacionController();
    var logisticaController = new LogisticaController();
    var adminController = new AdminController();
    var donanteController = new DonanteController();
    var telemetriaController = new TelemetriaController();

    app.before(this::cargarSesion);

    // rutas publicas
    app.get("/", home::landing);
    app.get("/legal", home::mostrarLegal);

    app.before("/login", this::noAutenticados);
    app.get("/login", session::login);
    app.post("/login", session::create);
    app.get("/login?registro=exitoso", session::login);

    app.before("/registro", this::noAutenticados);
    app.get("/registro", session::mostrarRegistro);
    app.post("/registro", session::registrarUsuario);

    app.get("/logout", session::logout);

    app.get("/mapa", donacionController::mostrarUbicacionDonaciones);
    app.get("/donaciones", donacionController::mostrarRegistroDonacion);
    app.post("/donaciones", donacionController::registrarDonacion);

    app.get("/donaciones-2", ctx -> {
      ctx.render("public/registro-donacion.hbs");
    });

    // app.before("/necesidades/*", new RolMiddleware(Rol.BENEFICIARIO)::validarPermisos);
    // app.before("/necesidades/*", this::autenticados);
    app.get("/necesidades", necesidadController::showNecesidades);
    app.post("/necesidades", necesidadController::crear);
    app.get("/necesidades/{id}", necesidadController::showNecesidad);
    app.post("/necesidades/{id}/peticiones", necesidadController::agregarPeticion);
    app.post("/necesidades/{id}/adjuntos", necesidadController::subirAdjunto);

    // las rutas comentadas son para poder ver las vistas sin loggear
    app.before("/usuarios/*", this::autenticados);
    app.before("/usuarios/*", new RolMiddleware(Rol.BENEFICIARIO)::validarPermisos);
    app.get("/usuarios/entregas-en-curso", logisticaController::entregasEnCurso);

    app.before("/admin/*", session::validarAutenticacion);
    app.before("/admin/*", new RolMiddleware(Rol.ADMIN)::validarPermisos);

    app.get("/admin/panel", adminController::administracion);

    app.get("/admin/logistica", logisticaController::mostrarCamiones);
    app.get("/admin/logistica/camion/{id}/panel", logisticaController::mostrarPanelCamion);
    app.get("/admin/logistica/entregas-en-curso", logisticaController::entregasEnCursoAdmin);
    app.get("/admin/logistica/deposito", logisticaController::obtenerDeposito);   // ?id= opcional sin id devuelve el deposito ppal
    app.post("/admin/logistica/deposito", logisticaController::guardarDeposito);
    app.get("/admin/logistica/depositos", logisticaController::listarDepositos);
    app.post("/camiones/{patente}/telemetria", telemetriaController::recepcionarTelemetria);
    app.get("/admin/logistica/ubicaciones", logisticaController::ubicacionesAdmin);

    app.get("/admin/donaciones/nueva", adminController::mostrarNuevaDonacion);
    app.get("/admin/donaciones", adminController::mostrarDonaciones);

    app.post("/admin/donaciones/{id}/asignar/{entidadId}", adminController::asignarDonacionManualmente);
    app.get("/admin/donaciones/{id}/entidades", adminController::mostrarEntidadesParaAsignacion);
    app.post("/admin/donaciones/vencidas",adminController::marcarVencidas);

    app.get("/admin/camiones/nuevo", logisticaController::mostrarFormCamion);
    app.post("/admin/camiones/nuevo", logisticaController::registrarCamion);

    app.post("/admin/donaciones", adminController::registrarDonacion);
    app.get("/admin/registro", session::mostrarRegistroAdmin);
    app.post("/admin/registro", session::registrarUsuario);
    app.post("/admin/donantes/importar", adminController::importarDonantesDesdeCsv);

    app.before("/donante/*", new RolMiddleware(Rol.DONANTE)::validarPermisos);
    app.get("/donante/donaciones",donanteController::misDonaciones);
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
      ctx.header("Set-Cookie", "session=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax");
      return;
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
