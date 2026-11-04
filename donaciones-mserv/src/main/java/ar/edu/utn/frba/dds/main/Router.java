package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.controllers.HomeController;
import ar.edu.utn.frba.dds.controllers.NecesidadController;
import ar.edu.utn.frba.dds.tareas.NotificarInactivos;
import io.javalin.Javalin;

import java.io.IOException;

public class Router {
  public void configure(Javalin app) throws IOException, InterruptedException {
    NecesidadController necesidadController = new NecesidadController();

    HomeController homeController = new HomeController();

    //Vistas

    app.get("/home", homeController::index);

    app.get("/", ctx -> ctx.redirect("/home"));

    /*PRUEBA DE ASIGNACION
    app.post("/donaciones/asignar", ctx ->
        {
          List<Map<String, Object>> resultado = donacionController.asignarDonacionANecesidad(ctx);
    ctx.json(resultado);});
    */

    //Necesidades

    app.post("/necesidades/",ctx ->
        ctx.status(201).json(necesidadController.crear(ctx)));
    app.post("/necesidades/{id}/peticiones/",ctx ->
        ctx.status(201).json(necesidadController.agregarPeticion(ctx)));

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