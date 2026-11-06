package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.EntidadDTO;
import ar.edu.utn.frba.dds.dto.entrada.AsignarDonacionDTO;
import ar.edu.utn.frba.dds.dto.salida.DonacionSegmentadaSalidaDTO;
import ar.edu.utn.frba.dds.dto.salida.PropuestasSalidaDTO;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.necesidad.Peticion;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.notificaciones.EventoNotificacionService;
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

  private final EventoNotificacionService notificaciones = EventoNotificacionService.Instance;

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

    if (!segmentada.getIdsEntidadesPropuestas().contains(entidad.getId())) {
      throw new BadRequestResponse("La entidad " + entidad.getId()
          + " no está entre las propuestas de la donación " + segmentada.getId());
    }

    Peticion peticion = entidad.getNecesidades().stream()
        .flatMap(necesidad -> necesidad.getPeticiones().stream())
        .filter(segmentada::puedeCubrir)
        .findFirst()
        .orElse(null);

    withTransaction(() -> segmentada.asignarA(entidad.getId(), peticion));

    avisarAsignacion(segmentada, entidad);

    return DonacionSegmentadaSalidaDTO.desde(segmentada);
  }

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
  private void avisarAsignacion(DonacionSegmentada segmentada, EntidadBeneficiaria entidad) {
    String nombreEntidad = entidad.nombreParaMostrar();

    Persona donante = segmentada.getDonante();
    if (donante != null) {
      try {
        notificaciones.notificarDonacionAsignadaDonante(
            donante, segmentada.getId(), nombreEntidad);
      } catch (Exception e) {
        System.err.println("[Asignacion] No se pudo avisar al donante de la donacion "
            + segmentada.getId() + ": " + e.getMessage());
      }
    } else {
      System.err.println("[Asignacion] La donacion " + segmentada.getId()
          + " no tiene donante resoluble: no se le avisa.");
    }

    try {
      notificaciones.notificarDonacionAsignadaBeneficiario(
          entidad, nombreEntidad, segmentada.getId());
    } catch (Exception e) {
      System.err.println("[Asignacion] No se pudo avisar a la entidad " + nombreEntidad
          + ": " + e.getMessage());
    }
  }
}
