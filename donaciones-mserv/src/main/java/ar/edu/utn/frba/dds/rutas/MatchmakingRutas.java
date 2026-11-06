package ar.edu.utn.frba.dds.rutas;

import ar.edu.utn.frba.dds.controllers.MatchmakingController;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class MatchmakingRutas {

  public void registrar(){
    MatchmakingController matchmakingController = new MatchmakingController();

    path("matchmaking",() -> {
      get("ranking", ctx ->
          ctx.status(200).json(matchmakingController.obtenerRanking(ctx)));
      post("asignaciones",ctx ->
          ctx.status(200).json(matchmakingController.asignarDonacion(ctx)));
      post("ejecuciones",ctx ->
          ctx.status(202).json(matchmakingController.procesarPendientes(ctx)));
    });
  }
}
