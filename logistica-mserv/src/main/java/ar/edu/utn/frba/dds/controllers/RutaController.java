package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.repositories.RutaRepositorio;
import io.javalin.http.Context;

import java.util.List;

public class RutaController {
  public Ruta showRuta(Context ctx) {
    var id = ctx.pathParam("id");
    return RutaRepositorio.Instance.findByid(id);
  }

  public List<Ruta> showAllRutas() {
    return RutaRepositorio.Instance.getAllRutas();
  }
}
