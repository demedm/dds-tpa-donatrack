package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.CambioEstadoDTO;
import ar.edu.utn.frba.dds.dto.salida.DonacionSegmentadaSalidaDTO;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

public class DonacionSegmentadaController implements WithSimplePersistenceUnit {


  public DonacionSegmentadaSalidaDTO cambiarEstadoDonacion(Context ctx) {
    Long id = ctx.pathParamAsClass("id",Long.class).get();
    CambioEstadoDTO cambioEstado = ctx.bodyAsClass(CambioEstadoDTO.class);

    DonacionSegmentada segmentada = DonacionesRepository.Instance.findSegmentadaById(id);

    if (segmentada == null) {
      throw new NotFoundResponse("Donacion segmentada " + id + "no encontrada");
    }

    withTransaction( () -> {
      if(cambioEstado.getMotivoFalla() != null) {
        segmentada.setJustificacionFallo(cambioEstado.getMotivoFalla());
      }
      segmentada.cambiarEstado(cambioEstado.getNuevoEstado());

    });

    return DonacionSegmentadaSalidaDTO.desde(segmentada);

  }




}