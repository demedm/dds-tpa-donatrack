package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.controllers.DonacionController;
import ar.edu.utn.frba.dds.controllers.DonacionSegmentadaController;
import ar.edu.utn.frba.dds.controllers.DonanteController;
import ar.edu.utn.frba.dds.controllers.EntidadBeneficiariaController;
import ar.edu.utn.frba.dds.controllers.EstadoEntregaController;
import ar.edu.utn.frba.dds.controllers.MatchmakingController;
import ar.edu.utn.frba.dds.controllers.NecesidadController;
import ar.edu.utn.frba.dds.model.Asignacion.AlgoritmoAsignacion;
import ar.edu.utn.frba.dds.model.Asignacion.AlgoritmoDeCompatibilidad;
import ar.edu.utn.frba.dds.model.Asignacion.AlgoritmoSubatendidos;
import ar.edu.utn.frba.dds.tareas.NotificarInactivos;
import io.javalin.Javalin;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Router {
  public void configure(Javalin app) throws IOException, InterruptedException {
    NecesidadController necesidadController = new NecesidadController();
    DonacionController donacionController = new DonacionController();
    DonanteController donanteController = new DonanteController();
    EntidadBeneficiariaController entidadController = new EntidadBeneficiariaController();
    DonacionSegmentadaController donacionSegmentadaController = new DonacionSegmentadaController();

    List<AlgoritmoAsignacion> algoritmos = List.of(new AlgoritmoDeCompatibilidad(),new AlgoritmoSubatendidos());

    MatchmakingController MatchmakingController = new MatchmakingController(algoritmos);

    EstadoEntregaController estadoEntregaController = new EstadoEntregaController();

    //Donaciones

    app.get("/donaciones/", ctx ->
        ctx.json(donacionController.mostrarDonaciones())
    );

    app.post("/donaciones",ctx->{
      ctx.status(201).json(donacionController.crearDonacion(ctx));
    });

    app.get("/donaciones/{id}",ctx-> {
      ctx.json(donacionController.mostrarDonacion(ctx));
    });

    app.delete("/donaciones/{id}",ctx->{
      donacionController.eliminar(ctx);
      ctx.status(204);
    });

    app.patch("/donacionesSegementada/{id}", ctx ->
        ctx.status(200).json(donacionSegmentadaController.cambiarEstadoDonacion(ctx))
    );


    //PRUEBA DE ASIGNACION
    app.post("/donaciones/asignar", ctx ->
        {
          List<Map<String, Object>> resultado = donacionController.asignarDonacionANecesidad(ctx);
    ctx.json(resultado);});

  //matchmaking

  /*
    app.get("/matchmaking/ranking/{idSegmentada}", ctx -> {
      ctx.status(200).json(MatchmakingController.obtenerRanking(ctx));
    });

    // Asignar donacionsementadaid y entidadbeneficiariaId
    app.post("/matchmaking/asignar",ctx->{
      ctx.status(201).json(MatchmakingController.asignarDonacion(ctx));
    });

    app.post("/matchmaking/procesar-pendientes", ctx ->
        ctx.status(201).json(MatchmakingController.procesarPendientes(ctx)));

  */
    //Necesidades

    app.post("/necesidades/",ctx ->
        ctx.status(201).json(necesidadController.crear(ctx)));
    app.post("/necesidades/{id}/peticiones/",ctx ->
        ctx.status(201).json(necesidadController.agregarPeticion(ctx)));

    app.get("/necesidades/recurrentes", ctx ->
        ctx.json(necesidadController.showNecesidadesRecurrentes()));
    app.get("/necesidades/", ctx ->
        ctx.status(201).json(necesidadController.showNecesidades()));
    app.get("/necesidades/{id}",ctx ->
        ctx.json(necesidadController.showNecesidad(ctx)));
      app.get("/test-cron", ctx -> {
    NecesidadController controller = new NecesidadController();
    controller.actualizarVencidas();
    ctx.result("Cron ejecutado manualmente");
});

    //Donante
    app.post("/donantes/fisicas", donanteController::crearDonanteFisica);
    app.post("/donantes/juridicas", donanteController::crearDonanteJuridica);
    app.get("/donantes/", ctx -> ctx.json(donanteController.obtenerDonantes(ctx)));
    app.get("/donantes/{email}", ctx -> ctx.json(donanteController.obtenerDonantePorEmail(ctx)));
    app.put("/donantes/{email}", ctx -> ctx.json(donanteController.actualizarDonante(ctx)));
    app.delete("/donantes/{email}", donanteController::eliminarDonante);

    //Entidades beneficiarias
    app.post("/entidades", entidadController::crearEntidad);
    app.get("/entidades/{id}", ctx -> ctx.json(entidadController.obtenerEntidad(ctx)));
    app.put("/entidades/{id}", ctx -> ctx.json(entidadController.actualizarEntidad(ctx)));
    app.delete("/entidades/{id}", entidadController::deleteEntidad);
    app.get("/entidades",ctx -> ctx.json(entidadController.obtenerEntidades()));

    app.put("/donaciones/{id}/estado", estadoEntregaController::cambiarEstado);

    app.post("/tareas/notificar-inactivos", ctx -> {
      int dias = ctx.queryParam("dias") != null ? Integer.parseInt(ctx.queryParam("dias")) : 20;
      ctx.json(NotificarInactivos.ejecutar(dias));
    });
  }
}