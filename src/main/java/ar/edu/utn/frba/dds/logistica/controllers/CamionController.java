package ar.edu.utn.frba.dds.logistica.controllers;

import ar.edu.utn.frba.dds.logistica.modelo.Camion;
import ar.edu.utn.frba.dds.logistica.modelo.Entrega;
import ar.edu.utn.frba.dds.logistica.repositories.CamionRepositorio;

import ar.edu.utn.frba.dds.logistica.repositories.RutaRepositorio;
import io.javalin.http.Context;

import java.util.List;

public class CamionController {
  public Camion randomCamion() {
    return CamionRepositorio.Instance.getRandom();
  }

  public Camion showCamion(Context ctx) {
    var patente = ctx.pathParam("patente");
    return CamionRepositorio.Instance.getByPatente(patente);
  }

  public List<Camion> showFlota() {
    return CamionRepositorio.Instance.getFlota();
  }

  public List<Entrega> showEntregas(Context ctx) {
    var patente = ctx.pathParam("patente");
    return CamionRepositorio.Instance
        .getByPatente(patente).getRutaActual().getEntregas();
  }

  public Entrega showEntrega(Context ctx) {
    var patente = ctx.pathParam("patente");
    var entregaId = Integer.parseInt(ctx.pathParam("id"));
    Camion camion = CamionRepositorio.Instance.getByPatente(patente);
    return RutaRepositorio.Instance.getById(camion, entregaId);
  }

  public void createCamion() {

  }

}
