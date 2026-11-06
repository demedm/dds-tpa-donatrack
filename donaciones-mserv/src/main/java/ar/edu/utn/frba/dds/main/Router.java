package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.controllers.DonanteController;
import ar.edu.utn.frba.dds.controllers.HomeController;
import ar.edu.utn.frba.dds.controllers.NecesidadController;
import ar.edu.utn.frba.dds.controllers.autenticacion.AuthController;
import ar.edu.utn.frba.dds.repositories.DonanteRepository;
import ar.edu.utn.frba.dds.tareas.NotificarInactivos;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.Javalin;

import java.io.IOException;

public class Router {
  public void configure(Javalin app) throws IOException, InterruptedException {
      WithSimplePersistenceUnit persistencia = new WithSimplePersistenceUnit() {};
      app.after("/necesidades", ctx -> persistencia.entityManager().clear());
      app.after("/necesidades/*", ctx -> persistencia.entityManager().clear());

      NecesidadController necesidadController = new NecesidadController();
      HomeController homeController = new HomeController();
      AuthController authController = new AuthController();
    DonanteController donanteController = new DonanteController();

    app.post("/auth/login", authController::login);
    app.post("/auth/registro", authController::registro);
    app.post("/donantes/importar", donanteController::importarDonantesDesdeCsv);
    //Vistas

    app.get("/home", homeController::index);

    app.get("/", ctx -> ctx.redirect("/home"));

    app.post("/necesidades/",necesidadController::crear);
    app.post("/necesidades/{id}/peticiones/",necesidadController::agregarPeticion);
    app.post("/necesidades/{id}/adjuntos", necesidadController::subirAdjunto);

    app.get("/necesidades/recurrentes", ctx ->
        ctx.json(necesidadController.showNecesidadesRecurrentes()));
      app.get("/necesidades", necesidadController::showNecesidades);
      app.get("/necesidades/{id}",necesidadController :: showNecesidad);
      app.get("/test-cron", ctx -> {
    NecesidadController controller = new NecesidadController();
    controller.actualizarVencidas();
    ctx.result("Cron ejecutado manualmente");
});

    app.post("/tareas/notificar-inactivos", ctx -> {
      int dias = ctx.queryParam("dias") != null ? Integer.parseInt(ctx.queryParam("dias")) : 20;
      ctx.json(NotificarInactivos.ejecutar(dias));
    });
  }


}