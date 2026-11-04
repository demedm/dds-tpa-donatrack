package ar.edu.utn.frba.dds.rutas;

import ar.edu.utn.frba.dds.controllers.EntidadBeneficiariaController;

import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.put;

public class EntidadRutas {

  public void registrar(){

    EntidadBeneficiariaController  entidadController = new EntidadBeneficiariaController();

    path("entidades",() -> {
      get(ctx -> ctx.json(entidadController.obtenerEntidades()));
      post(entidadController::crearEntidad);
      path("{id}",() -> {
        get(ctx -> ctx.json(entidadController.obtenerEntidad(ctx)));
        put(ctx -> ctx.json(entidadController.actualizarEntidad(ctx)));
        delete(entidadController::deleteEntidad);
      });
    });
  }
}
