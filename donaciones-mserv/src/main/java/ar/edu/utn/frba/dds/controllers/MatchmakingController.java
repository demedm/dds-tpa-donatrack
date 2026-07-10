package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.AsignarDonacionDTO;
import ar.edu.utn.frba.dds.dto.DonacionsDTO;
import ar.edu.utn.frba.dds.dto.ResultadosMatchmakingDTO;
import ar.edu.utn.frba.dds.model.Asignacion.Resultados;
import ar.edu.utn.frba.dds.model.Asignacion.ServicioMatchmaking;
import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.repositories.*;
import ar.edu.utn.frba.dds.model.Estado.*;
import ar.edu.utn.frba.dds.model.necesidad.*;
import java.util.ArrayList;
import java.util.List;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

public class MatchmakingController {

  //Inyeccion de dependencia

  private ServicioMatchmaking ServicioMatchmaking;

  public MatchmakingController(ServicioMatchmaking ServicioMatchmaking) {
    this.ServicioMatchmaking = ServicioMatchmaking;
  }

  //donacion Segmentada

  public Resultados obtenerRanking(Context ctx){
    String idSegmentada = ctx.pathParam("id");
    DonacionSegmentada donacion = DonacionesRepository.Instance.findSegmentadaById(idSegmentada);
    if(!donacion.estaAlmacen()){
      throw new NotFoundResponse("La donación no se encuentra EN_DEPOSITO");
    }
    if(donacion == null){
      throw new NotFoundResponse("DonacionSegmentada no encontrada");
    };

    List<EntidadBeneficiaria> entidades = EntidadRepository.Instance.obtenerEntidades();
    Resultados resultados = ServicioMatchmaking.ejecutar(donacion, entidades);

    return resultados;
  }


  public DonacionSegmentada asignarDonacion(Context ctx) {

    AsignarDonacionDTO dto = ctx.bodyAsClass(AsignarDonacionDTO.class);

    String idDonacion = dto.getDonacionSegmentadaId();
    String idEntidad = dto.getEntidadBeneficiariaId();

    DonacionSegmentada segmentada = DonacionesRepository.Instance.findSegmentadaById(idDonacion);

    if(segmentada == null){
      throw new NotFoundResponse("No se encontro el donacion segmentada con ese ID");
    }
    if(segmentada.estaAlmacen()){
      throw new NotFoundResponse("Solo se puede asignar donaciones en estado EN_DEPOSITO");
    }

    EntidadBeneficiaria entidad = EntidadRepository.Instance.obtenerPorId(idEntidad);
    if(entidad == null){
      throw new NotFoundResponse("No se encontro el entidad con ese ID");
    }

    segmentada.asignar();
    segmentada.setEntidadAsignadaId(idEntidad);

    return segmentada;

  }

}
