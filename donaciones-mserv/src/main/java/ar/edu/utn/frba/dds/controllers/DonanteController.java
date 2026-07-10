package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.PersonaDTO;
import ar.edu.utn.frba.dds.dto.PersonaFisicaDTO;
import ar.edu.utn.frba.dds.dto.PersonaJuridicaDTO;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.donantes.PersonaFisica;

import ar.edu.utn.frba.dds.repositories.DonanteRepository;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.util.List;

public class DonanteController {

  private DonanteRepository registroDonante = DonanteRepository.Instance;

  private void crearDonante(Context ctx, PersonaDTO dto) {
    Persona nuevoDonante = dto.PersonaDto();
    registroDonante.registrarDonante(nuevoDonante);
    ctx.status(201).json(nuevoDonante);
  }

  public void crearDonanteFisica(Context ctx) {

    crearDonante(ctx, ctx.bodyAsClass(PersonaFisicaDTO.class));

  }

  public void crearDonanteJuridica(Context ctx) {

    crearDonante(ctx, ctx.bodyAsClass(PersonaJuridicaDTO.class));

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
    Persona existente = registroDonante.buscarPorEmail(email)
        .orElseThrow(() -> new NotFoundResponse("No existe un donante con email " + email));

    PersonaDTO dto = existente instanceof PersonaFisica ? ctx.bodyAsClass(PersonaFisicaDTO.class) : ctx.bodyAsClass(PersonaJuridicaDTO.class);

    dto.aplicarCambios(existente);
    return existente;
  }

  public void eliminarDonante(Context ctx) {
    var email = ctx.pathParam("email");
    boolean eliminado = registroDonante.eliminarPorEmail(email);
    ctx.status(eliminado ? 204 : 404);
  }
}
