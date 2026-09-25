package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.repositories.EntregaRepositorio;
import ar.edu.utn.frba.dds.repositories.RutaRepositorio;
import ar.edu.utn.frba.dds.scripts.dto.RutaDto;
import io.javalin.http.Context;
import java.util.List;
import java.util.Objects;

public class RutaController {

  public Ruta showRuta(Context ctx) {
    var id = Long.parseLong(ctx.pathParam("id"));
    return RutaRepositorio.Instance.buscarPorId(id);
  }

  public Entrega showEntrega(Context ctx) {
    // String idRuta = ctx.pathParam("idRuta");
    var idEntrega = Long.parseLong(ctx.pathParam("idEntrega"));
    return EntregaRepositorio.Instance.buscarPorId(idEntrega);
  }

  public List<Entrega> showEntregas(Context ctx) {
    var idRuta = Long.parseLong(ctx.pathParam("id"));
    return EntregaRepositorio.Instance.buscarEntregasDeRuta(idRuta);
  }

  public List<Ruta> showAllRutas() {
    return RutaRepositorio.Instance.mostrarTodos();
  }

  public void postRuta(Context ctx) {
    Ruta ruta = validarRuta(ctx);
    RutaRepositorio.Instance.registrar(ruta);
    ctx.status(201);
  }

  public Ruta postEntrega(Context ctx) {
    Long idRuta = Long.parseLong(ctx.pathParam("idRuta"));
    Entrega entrega = ctx.bodyValidator(Entrega.class)
        .check(e -> e.getId() != null,
            "El id de la entrega es obligatorio")
        .check(e -> e.getDireccion() != null && !e.getDireccion().isBlank(),
            "La dirección es obligatoria")
        .check(e -> e.getDonacionId() != null,
            "El id de la donación debe ser mayor a 0")
        .get();
    Ruta ruta = RutaRepositorio.Instance.buscarPorId(idRuta);
    if (ruta == null) {
      ctx.status(404);
      return null;
    }
    EntregaRepositorio.Instance.registrar(entrega);
    ctx.status(201);
    ruta.agregarEntrega(entrega);
    // RutaRepositorio.Instance.actualizar(ruta);
    return ruta;
  }

  public void patchRuta(Context ctx) {
    Long idRuta = Long.parseLong(ctx.pathParam("id"));
    Ruta ruta = RutaRepositorio.Instance.buscarPorId(idRuta);
    if (ruta == null) {
      ctx.status(404);
      return;
    }

    RutaDto rutaModificada = ctx.bodyAsClass(RutaDto.class);

    var rutaActualizada = RutaRepositorio.Instance.actualizarRuta(ruta, rutaModificada);
    if (rutaActualizada == null) {
      ctx.status(400);
      return;
    }
    ctx.status(200);
    ctx.json(rutaActualizada);
  }

  public void deleteRuta(Context ctx) {
    Long idRuta = Long.parseLong(ctx.pathParam("id"));
    Ruta ruta = RutaRepositorio.Instance.buscarPorId(idRuta);

    RutaRepositorio.Instance.eliminarRutaYEntregas(ruta);
    ctx.status(200);
  }

  private Ruta validarRuta(Context ctx) {
    return ctx.bodyValidator(Ruta.class)
        .check(r -> r.getCamion() != null,
            "El camión es obligatorio")
        .check(r -> r.getEntregas() != null && !r.getEntregas().isEmpty(),
            "La ruta debe contener al menos una entrega")
        .check(r -> r.getEntregas().stream().allMatch(e ->
                e.getId() != null),
            "Todas las entregas deben tener un id")
        .check(r -> r.getEntregas().stream().allMatch(e ->
                e.getDireccion() != null && !e.getDireccion().isBlank()),
            "Todas las entregas deben tener una dirección")
        .check(r -> r.getEntregas().stream().allMatch(e ->
                e.getDonacionId() != null),
            "Todas las entregas deben tener un id de donación válido")
        .check(r -> r.getChofer() != null,
            "Las rutas tienen que tener asignado un chofer")
        .get();
  }

}
