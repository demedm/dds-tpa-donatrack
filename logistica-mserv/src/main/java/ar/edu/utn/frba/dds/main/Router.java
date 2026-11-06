package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.controllers.CallbackController;
import ar.edu.utn.frba.dds.controllers.CamionController;
import ar.edu.utn.frba.dds.controllers.DashboardController;
import ar.edu.utn.frba.dds.controllers.MonitoreoController;
import ar.edu.utn.frba.dds.controllers.RutaController;
import ar.edu.utn.frba.dds.controllers.UsuarioController;
import io.javalin.Javalin;

public class Router {
  public void configure(Javalin app) {
    MonitoreoController monitoreoController = new MonitoreoController();
    CamionController camionController = new CamionController();
    RutaController rutaController = new RutaController();
    DashboardController dashboardController = new DashboardController();
    CallbackController callbackController = new CallbackController();
    UsuarioController usuarioController = new UsuarioController();

    // DASHBOARD DE MONITOREO
    app.get("/dashboard", ctx -> ctx.render("dashboard.hbs"));
    app.get("/dashboard/camiones", dashboardController::listarCamiones);
    app.post("/camiones/{patente}/telemetria", monitoreoController::recepcionarTelemetria);

    // CRUD CALLBACK
    app.post("/callback/planificaciones", callbackController::recibirPlanificacion);

    // CRUD CAMIONES
    app.get("/camiones/random", ctx -> ctx.json(camionController.randomCamion()));
    app.get("/camiones", ctx -> ctx.json(camionController.showFlota(ctx)));
    app.get("/camiones/{id}", camionController::buscarPorId);
    app.post("/camiones/{patente}", camionController::saveCamion);
    app.post("/camiones/{id}/imprevisto-logistico", camionController::reportarImprevisto);
    app.put("/camiones/{patente}", camionController::modificarCamion);
    app.post("/camiones", camionController::registrarCamion);

    // CRUD RUTAS Y ENTREGAS
    app.get("/rutas", ctx -> ctx.json(rutaController.showAllRutas()));
    app.get("/rutas/{id}", ctx -> ctx.json(rutaController.showRuta(ctx)));
    app.get("/rutas/{idRuta}/entregas/{idEntrega}", ctx -> ctx.json(
        rutaController.showEntrega(ctx)));
    app.get("/rutas/{id}/entregas", ctx -> ctx.json(
        rutaController.showEntregas(ctx)));
    app.post("/rutas", rutaController::postRuta);
    app.patch("/rutas/{id}", rutaController::patchRuta);
    app.delete("/rutas/{id}", rutaController::deleteRuta);

    app.post("/usuarios/{idUsuario}/entregas/{id}/confirmar",
        usuarioController::confirmarEntrega);
    app.post("/usuarios/{idUsuario}/entregas/{id}/no-entregado",
        usuarioController::marcarEntregaComoNoRecepcionada);
    app.post("/usuarios/{idUsuario}/rutas/{id}/iniciar",
        usuarioController::iniciarRuta);
    app.get("/usuarios/{id}", usuarioController::showUsuario);
    app.post("/usuarios", usuarioController::guardarUsuario);

    // RECEPCION DE DONACIONES
    app.post("/donaciones/{id}", callbackController::recibirDonacion);
// RECEPCION MASIVA DE DONACIONES PARA PLANIFICAR
    app.post("/planificaciones/lotes", callbackController::recibirLotePlanificacion);

    app.patch("/entregas/{idEntrega}/recepcionar", rutaController::recepcionarEntrega);
    app.patch("/entregas/{idEntrega}/rechazar", rutaController::rechazarEntrega);
  }
}