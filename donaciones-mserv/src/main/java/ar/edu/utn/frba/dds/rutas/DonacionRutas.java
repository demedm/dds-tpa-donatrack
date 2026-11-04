package ar.edu.utn.frba.dds.rutas;

import ar.edu.utn.frba.dds.controllers.DonacionController;
import ar.edu.utn.frba.dds.controllers.DonacionSegmentadaController;
import ar.edu.utn.frba.dds.controllers.EstadoEntregaController;

import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.patch;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.put;

public class DonacionRutas {

  public void registrar(){

    DonacionController donacionController = new DonacionController();
    DonacionSegmentadaController donacionSegmentadaController = new DonacionSegmentadaController();
    EstadoEntregaController estadoEntregaController = new EstadoEntregaController();

    path("donaciones", () ->{
      get(ctx -> ctx.json(donacionController.mostrarDonaciones()));

      post(ctx -> ctx.status(201).json(donacionController.crearDonacion(ctx)));
      path("{id}", () ->{
        get(ctx -> ctx.json(donacionController.mostrarDonacion(ctx)));
        delete(ctx -> {
            donacionController.eliminar(ctx);
            ctx.status(204);
        });

        // Logística avisa acá cada cambio de estado: se guarda y se notifica al donante y a la entidad
        put("estado",estadoEntregaController::cambiarEstado);
      });
    });

    patch("donacionesSegmentada/{id}", ctx ->
        ctx.status(200).json(donacionSegmentadaController.cambiarEstadoDonacion(ctx)));

  }

}
