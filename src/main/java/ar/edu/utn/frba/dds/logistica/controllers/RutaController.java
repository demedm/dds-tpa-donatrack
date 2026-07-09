package ar.edu.utn.frba.dds.logistica.controllers;

import ar.edu.utn.frba.dds.logistica.modelo.Ruta;
import ar.edu.utn.frba.dds.logistica.repositories.RutaRepositorio;
import io.javalin.http.Context;

import java.util.List;

public class RutaController {
  public Ruta showRuta(Context ctx) {
    var id = Integer.parseInt(ctx.pathParam("id"));
    return RutaRepositorio.Instance.findByid(id);
  }

  public List<Ruta> showAllRutas() {
    return RutaRepositorio.Instance.getAllRutas();
  }
}
