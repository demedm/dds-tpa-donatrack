package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.EntidadDTO;
import ar.edu.utn.frba.dds.dto.entrada.AsignarDonacionDTO;
import ar.edu.utn.frba.dds.dto.salida.DonacionSegmentadaSalidaDTO;
import ar.edu.utn.frba.dds.dto.salida.PropuestasSalidaDTO;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.necesidad.Peticion;
import ar.edu.utn.frba.dds.repositories.*;

import java.util.List;
import java.util.Map;

import ar.edu.utn.frba.dds.tareas.ProcesarDonaciones;
import ar.edu.utn.frba.dds.utils.AsignacionCronScheduler;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.UnauthorizedResponse;

public class MatchmakingController implements WithSimplePersistenceUnit {

  public PropuestasSalidaDTO obtenerRanking(Context ctx) {
    Long idSegmentada = ctx.queryParamAsClass("donacionSegmentadaId", Long.class).get();
    DonacionSegmentada segmentada = DonacionesRepository.Instance.findSegmentadaById(idSegmentada);

    if (segmentada == null) {
      throw new NotFoundResponse("DonacionSegmentada " + idSegmentada + " no encontrada");
    }
    if (!segmentada.estaAlmacen()) {
      throw new BadRequestResponse("La donación" + idSegmentada + "no se encuentra EN_DEPOSITO");
    }

    return PropuestasSalidaDTO.desde(segmentada, id -> {
      EntidadBeneficiaria entidad = EntidadRepository.Instance.obtenerPorId(id);
      return entidad == null ? null : EntidadDTO.desde(entidad);
    });
  }

  // El administrador elige la entidad final
  public DonacionSegmentadaSalidaDTO asignarDonacion(Context ctx) {
    AsignarDonacionDTO dto = ctx.bodyAsClass(AsignarDonacionDTO.class);

    DonacionSegmentada segmentada = DonacionesRepository.Instance.findSegmentadaById(dto.getDonacionSegmentadaId());
    if (segmentada == null) {
      throw new NotFoundResponse("No se encontró la donación segmentada " + dto.getDonacionSegmentadaId());
    }
    if (!segmentada.estaAlmacen()) {
      throw new BadRequestResponse("Solo se pueden asignar donaciones EN_DEPOSITO (donación " + dto.getDonacionSegmentadaId() + ")");
    }

    EntidadBeneficiaria entidad = EntidadRepository.Instance.obtenerPorId(dto.getEntidadBeneficiariaId());
    if (entidad == null) {
      throw new NotFoundResponse("No se encontró la entidad " + dto.getEntidadBeneficiariaId());
    }

    // La primera petición de la entidad que esta donación puede cubrir (o ninguna)
    Peticion peticion = entidad.getNecesidades().stream()
        .flatMap(necesidad -> necesidad.getPeticiones().stream())
        .filter(segmentada::puedeCubrir)
        .findFirst()
        .orElse(null);

    // Se compromete la cantidad; se suma como recibida recién cuando se confirma la entrega
    withTransaction(() -> segmentada.asignarA(entidad.getId(), peticion));

    return DonacionSegmentadaSalidaDTO.desde(segmentada);
  }

  // Ejecución a demanda: valida el token y lanza el matchmaking en otro hilo
  public Map<String, Object> procesarPendientes(Context ctx) {
    validarTokenDeJob(ctx);
    AsignacionCronScheduler.ejecutarAhora();
    return Map.of("resultado", "matchmaking iniciado");
  }

  private void validarTokenDeJob(Context ctx) {
    String tokenEsperado = System.getenv("MATCHMAKING_JOB_TOKEN");
    if (tokenEsperado == null || !tokenEsperado.equals(ctx.header("X-Job-Token"))) {
      throw new UnauthorizedResponse("No autorizado");
    }
  }
}
