package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.EntidadDTO;
import ar.edu.utn.frba.dds.dto.entrada.AsignarDonacionDTO;
import ar.edu.utn.frba.dds.dto.salida.DonacionSegmentadaSalidaDTO;
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

  public List<EntidadDTO> obtenerRanking(Context ctx) {
    Long idSegmentada = ctx.queryParamAsClass("donacionSegmentadaId", Long.class).get();
    DonacionSegmentada segmentada = DonacionesRepository.Instance.findSegmentadaById(idSegmentada);

    if (segmentada == null) {
      throw new NotFoundResponse("DonacionSegmentada"+ idSegmentada + " no se encuentra EN_DEPOSITO ");
    }
    if (!segmentada.estaAlmacen()) {
      throw new BadRequestResponse("La donación" + idSegmentada + "no se encuentra EN_DEPOSITO");
    }

    return segmentada.getIdsEntidadesPropuestas().stream()
        .map(EntidadRepository.Instance::obtenerPorId)
        .filter(entidad -> entidad != null)
        .map(EntidadDTO::desde)
        .toList();
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

    withTransaction(() -> {
      entidad.getNecesidades().stream()
          .flatMap(necesidad -> necesidad.getPeticiones().stream())
          .filter(segmentada::puedeCubrir)
          .findFirst()
          .ifPresent(peticion ->
              peticion.agregarCantidadRecibida(segmentada.getCantidad(), String.valueOf(segmentada.getId())));
      segmentada.asignarA(entidad.getId());
    });

    return DonacionSegmentadaSalidaDTO.desde(segmentada);
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
