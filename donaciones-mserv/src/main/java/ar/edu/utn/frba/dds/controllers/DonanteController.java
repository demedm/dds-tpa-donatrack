package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.entrada.PersonaDTO;
import ar.edu.utn.frba.dds.dto.entrada.PersonaFisicaDTO;
import ar.edu.utn.frba.dds.dto.entrada.PersonaJuridicaDTO;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.donantes.PersonaFisica;

import ar.edu.utn.frba.dds.model.notificaciones.EventoNotificacionService;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;
import ar.edu.utn.frba.dds.repositories.DonanteRepository;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.UploadedFile;
import org.jetbrains.annotations.NotNull;

import java.net.http.HttpResponse;
import java.util.List;

public class DonanteController implements WithSimplePersistenceUnit {

  private final DonanteRepository registroDonante = DonanteRepository.Instance;
  private final EventoNotificacionService notificaciones = EventoNotificacionService.Instance;

  private void crearDonante(Context ctx, PersonaDTO dto) {
    Persona nuevoDonante = dto.PersonaDto();

    boolean esNuevo = withTransaction(() ->
      registroDonante.registrarDonante(nuevoDonante));

    if (esNuevo) {
      ctx.status(201).json(nuevoDonante);
    }

    ctx.status(204).json(nuevoDonante);

  }

  private void enviarBienvenida(Persona donante) {
    try{
      notificaciones.notificarBienvenida(donante);
    }catch(Exception e){
      // El alta ya está confirmada: que falle el mail no la invalida
      System.err.println("[Bienvenida] No se pudo notificar a "
          + donante.getNombreIdentificador() + ": " + e.getMessage());
    }
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

    PersonaDTO dto = existente instanceof PersonaFisica
        ? ctx.bodyAsClass(PersonaFisicaDTO.class)
        : ctx.bodyAsClass(PersonaJuridicaDTO.class);

    withTransaction(() ->dto.aplicarCambios(existente));
    return existente;
  }

  public void eliminarDonante(Context ctx) {
    var email = ctx.pathParam("email");
    boolean eliminado = registroDonante.eliminarPorEmail(email);
    ctx.status(eliminado ? 204 : 404);
  }

  public void importarDonantesDesdeCsv(Context ctx) {
    try {
      UploadedFile archivo = ctx.uploadedFile("archivo");

      if (archivo == null) {
        ctx.status(400);
        return;
      }

      DonanteRepository.Instance.importarDonantes(archivo);
      ctx.status(200);

    } catch (Exception e) {
      ctx.status(400);
    }
  }
}
