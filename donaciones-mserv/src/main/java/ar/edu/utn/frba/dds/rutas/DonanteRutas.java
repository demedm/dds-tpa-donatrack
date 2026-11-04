package ar.edu.utn.frba.dds.rutas;

import ar.edu.utn.frba.dds.controllers.DonanteController;

import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.put;

public class DonanteRutas {
  public  void registrar(){
    DonanteController donanteController = new DonanteController();

    path("donantes", () -> {
      get(ctx -> ctx.json(donanteController.obtenerDonantes(ctx)));
      post("fisicas", donanteController::crearDonanteFisica);
      post("juridicas", donanteController::crearDonanteJuridica);
      path("{email}", () -> {
        get(ctx -> ctx.json(donanteController.obtenerDonantePorEmail(ctx)));
        put(ctx -> ctx.json(donanteController.actualizarDonante(ctx)));
        delete(donanteController::eliminarDonante);
      });
    });
  }
}
