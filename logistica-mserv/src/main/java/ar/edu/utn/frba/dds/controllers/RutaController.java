package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.repositories.RutaRepositorio;
import io.javalin.http.Context;

import java.util.List;

public class RutaController {
  public Ruta showRuta(Context ctx) {
    var id = ctx.pathParam("id");
    return RutaRepositorio.Instance.findByid(id);
  }

  public Entrega showEntrega(Context ctx) {
    String idRuta = ctx.pathParam("idRuta");
    String idEntrega = ctx.pathParam("idEntrega");
    return RutaRepositorio.Instance.findEntregaById(idRuta, idEntrega);
  }

  public List<Entrega> showEntregas(Context ctx) {
    String idRuta = ctx.pathParam("id");
    var ruta = RutaRepositorio.Instance.findByid(idRuta);
    return ruta.getEntregas();
  }

  public List<Ruta> showAllRutas() {
    return RutaRepositorio.Instance.getAllRutas();
  }

  public Ruta saveRuta(Context ctx) {
    Ruta ruta = ctx.bodyAsClass(Ruta.class);
    RutaRepositorio.Instance.addRuta(ruta);
    return ruta;
  }

  public Entrega saveEntrega(Context ctx) {
    String idRuta = ctx.pathParam("idRuta");
    Entrega entrega = ctx.bodyAsClass(Entrega.class);
    RutaRepositorio.Instance.findByid(idRuta).agregarEntrega(entrega);
    return entrega;
  }

}
