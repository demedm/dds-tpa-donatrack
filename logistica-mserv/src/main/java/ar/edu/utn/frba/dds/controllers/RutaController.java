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
    Ruta ruta = ctx.bodyValidator(Ruta.class)
        .check(r -> r.getId() != null && !r.getId().isBlank(),
            "El id de la ruta es obligatorio")
        .check(r -> r.getPatenteAsignada() != null && !r.getPatenteAsignada().isBlank(),
            "La patente del camión es obligatoria")
        .check(r -> r.getEntregas() != null && !r.getEntregas().isEmpty(),
            "La ruta debe contener al menos una entrega")
        .check(r -> r.getEntregas().stream().allMatch(e ->
                e.getId() != null && !e.getId().isBlank()),
            "Todas las entregas deben tener un id")
        .check(r -> r.getEntregas().stream().allMatch(e ->
                e.getDireccion() != null && !e.getDireccion().isBlank()),
            "Todas las entregas deben tener una dirección")
        .check(r -> r.getEntregas().stream().allMatch(e ->
                e.getDonacionId() > 0),
            "Todas las entregas deben tener un id de donación válido")
        .get();
    RutaRepositorio.Instance.addRuta(ruta);
    return ruta;
  }

  public Ruta saveEntrega(Context ctx) {
    String idRuta = ctx.pathParam("idRuta");
    Entrega entrega = ctx.bodyValidator(Entrega.class)
        .check(e -> e.getId() != null && !e.getId().isBlank(),
            "El id de la entrega es obligatorio")
        .check(e -> e.getDireccion() != null && !e.getDireccion().isBlank(),
            "La dirección es obligatoria")
        .check(e -> e.getDonacionId() > 0,
            "El id de la donación debe ser mayor a 0")
        .get();
    Ruta ruta = RutaRepositorio.Instance.findByid(idRuta);
    if(ruta == null) {
      ctx.status(404);
      return null;
    }
    ruta.agregarEntrega(entrega);
    return ruta;
  }

}
