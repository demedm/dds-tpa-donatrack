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
    Camion camion = tryToFindCamion(patente, ctx);
    if(camion != null) {
      ctx.status(200);
    }
    return camion;
  }

  public List<Camion> showFlota() {
    return CamionRepositorio.Instance.getFlota();
  }

  public List<Entrega> showEntregas(Context ctx) {
    var patente = ctx.pathParam("patente");
    Camion camion = tryToFindCamion(patente, ctx);
    if(camion != null) {
      ctx.status(200);
      return camion.getRutaActual().getEntregas();
    }
    return null;
  }

  public void buscarPorPatente(Context ctx) {
    String patente = ctx.pathParam("patente");
    Camion camion = CamionRepositorio.Instance.findByPatente(patente);

    if (camion == null) {
      ctx.status(404);
      return;
    }

    ctx.json(camion);
  }

  private Camion tryToFindCamion(String patente, Context ctx) {
    if(patente.isEmpty()) {
      ctx.status(400);
      return null;
    }
    var camion = CamionRepositorio.Instance.findByPatente(patente);
    if(camion == null) {
      ctx.status(404);
      return null;
    }
    return camion;
  }

  public void saveCamion(Context ctx) {
    Camion camion = ctx.bodyValidator(Camion.class)
        .check(c -> c.getPatente() != null && !c.getPatente().isBlank(),
            "La patente es obligatoria")
        .check(c -> c.getCapacidadVolumen() > 0,
            "La capacidad de volumen debe ser mayor a 0")
        .check(c -> c.getAltura() > 0,
            "La altura debe ser mayor a 0")
        .check(c -> c.getCapacidadCarga() > 0,
            "La capacidad de carga debe ser mayor a 0")
        .get();
    CamionRepositorio.Instance.registrarCamion(camion);
    ctx.status(201); // CREATED
    ctx.json(camion);
  }

}