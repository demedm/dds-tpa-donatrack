package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.AsignarDonacionDTO;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.repositories.*;

import java.util.List;
import java.util.Map;

import ar.edu.utn.frba.dds.tareas.ProcesarDonaciones;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.UnauthorizedResponse;

public class MatchmakingController implements WithSimplePersistenceUnit {

  public List<EntidadBeneficiaria> obtenerRanking(Context ctx) {
    Long idSegmentada = ctx.pathParamAsClass("idSegmentada", Long.class).get();
    DonacionSegmentada segmentada = DonacionesRepository.Instance.findSegmentadaById(idSegmentada);

    if (segmentada == null) {
      throw new NotFoundResponse("DonacionSegmentada no encontrada");
    }
    if (!segmentada.estaAlmacen()) {
      throw new BadRequestResponse("La donación no se encuentra EN_DEPOSITO");
    }

    return segmentada.getIdsEntidadesPropuestas().stream()
        .map(EntidadRepository.Instance::obtenerPorId)
        .filter(entidad -> entidad != null)
        .toList();
  }

  // El administrador elige la entidad final
  public DonacionSegmentada asignarDonacion(Context ctx) {
    AsignarDonacionDTO dto = ctx.bodyAsClass(AsignarDonacionDTO.class);

    DonacionSegmentada segmentada = DonacionesRepository.Instance.findSegmentadaById(dto.getDonacionSegmentadaId());
    if (segmentada == null) {
      throw new NotFoundResponse("No se encontró la donación segmentada con ese ID");
    }
    if (!segmentada.estaAlmacen()) {
      throw new BadRequestResponse("Solo se pueden asignar donaciones en estado EN_DEPOSITO");
    }

    EntidadBeneficiaria entidad = EntidadRepository.Instance.obtenerPorId(dto.getEntidadBeneficiariaId());
    if (entidad == null) {
      throw new NotFoundResponse("No se encontró la entidad con ese ID");
    }

    withTransaction(() -> {
      entidad.getNecesidades().stream()
          .flatMap(necesidad -> necesidad.getPeticiones().stream())
          .filter(segmentada::puedeCubrir)
          .findFirst()
          .ifPresent(peticion ->
              peticion.agregarCantidadRecibida(segmentada.getCantidad(), String.valueOf(segmentada.getId())));
      segmentada.asignarA(entidad.getId());
    });

    return segmentada;
  }

  // Ejecución a demanda: valida el token y lanza el matchmaking en otro hilo
  public Map<String, Object> procesarPendientes(Context ctx) {
    validarTokenDeJob(ctx);
    new Thread(ProcesarDonaciones::ejecutar).start();
    return Map.of("resultado", "matchmaking iniciado");
  }

  private void validarTokenDeJob(Context ctx) {
    String tokenEsperado = System.getenv().getOrDefault("MATCHMAKING_JOB_TOKEN", "dev_secret_local");
    if (!tokenEsperado.equals(ctx.header("X-Job-Token"))) {
      throw new UnauthorizedResponse("No autorizado");
    }
  }
}
