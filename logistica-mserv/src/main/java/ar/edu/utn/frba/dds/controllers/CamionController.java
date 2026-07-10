package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.repositories.CamionRepositorio;

import ar.edu.utn.frba.dds.repositories.RutaRepositorio;
import io.javalin.http.Context;

import java.util.List;

public class CamionController {
  public Camion randomCamion() {
    return CamionRepositorio.Instance.getRandom();
  }

  public Camion showCamion(Context ctx) {
    var patente = ctx.pathParam("patente");
    return CamionRepositorio.Instance.findByPatente(patente);
  }

  public List<Camion> showFlota() {
    return CamionRepositorio.Instance.getFlota();
  }

  public List<Entrega> showEntregas(Context ctx) {
    var patente = ctx.pathParam("patente");
    return CamionRepositorio.Instance
        .findByPatente(patente).getRutaActual().getEntregas();
  }

}