package ar.edu.utn.frba.dds.Donaciones.DonacionesRouter;

import ar.edu.utn.frba.dds.Donaciones.Donacion;
import ar.edu.utn.frba.dds.Donaciones.DonacionesDominio.DonacionesRepository;
import io.javalin.Javalin;

public class DonacionesRouter {

  private DonacionesRepository repository;

  public DonacionesRouter(DonacionesRepository repository) {
    this.repository = repository;
  }

  public void registrarRouter(Javalin app) {
    app.get("/donaciones",ctx ->
        ctx.json(repository.obtenerTodas()));

    app.get("/donaciones/{id})",ctx-> {
      Donacion donacion =repository.obtenerPorId(ctx.pathParam("id"));
      if(donacion == null) ctx.status(400);
      else ctx.json(donacion);
    });

    app.delete("/donaciones/{id}",ctx->{
      repository.eliminar(ctx.pathParam("id"));
      ctx.status(200);
    });



  }


}
