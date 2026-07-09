package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.repositories.CamionRepositorio;

import io.javalin.http.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CamionController {
  public Camion randomCamion() {
    return CamionRepositorio.Instance.getRandom();
  }

  public Camion showCamion(Context ctx) {
    var patente = ctx.pathParam("patente");
    return CamionRepositorio.Instance.getByPatente(patente);
  }

}
