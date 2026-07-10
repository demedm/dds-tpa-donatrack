package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.donantes.Persona;

import ar.edu.utn.frba.dds.repositories.DonanteRepository;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.util.List;

public class DonanteController {

  private DonanteRepository registroDonante = DonanteRepository.Instance;

  public void crearDonante(Context ctx){
    Persona nuevoDonante = ctx.bodyAsClass(Persona.class);
    registroDonante.registrarDonante(nuevoDonante);
    ctx.status(201).json(nuevoDonante);
  }

  public List<Persona> obtenerDonantes(Context ctx){
    return registroDonante.getRegistroDonantes();
  }

  public Persona obtenerDonantePorEmail(Context ctx) {
    var email = ctx.pathParam("email");
        return registroDonante.buscarPorEmail(email)
    .orElseThrow(() -> new NotFoundResponse("No existe un donante con email " + email));
  }

  public Persona actualizarDonante(Context ctx){
    var email = ctx.pathParam("email");
    Persona datosNuevos = ctx.bodyAsClass(Persona.class);
    Persona existente = registroDonante.buscarPorEmail(email)
        .orElseThrow(() -> new NotFoundResponse("No existe un donante con email " + email));
    existente.actualizarInfo(datosNuevos);
    return existente;
  }

  public void eliminarDonante(Context ctx) {
    var email = ctx.pathParam("email");
    boolean eliminado = registroDonante.eliminarPorEmail(email);
    ctx.status(eliminado ? 204 : 404);
  }
}
