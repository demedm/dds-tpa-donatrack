package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.AsignarDonacionDTO;
import ar.edu.utn.frba.dds.model.Asignacion.AlgoritmoAsignacion;
import ar.edu.utn.frba.dds.model.Asignacion.Resultados;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.repositories.*;

import java.util.List;
import java.util.Map;

import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

public class MatchmakingController {

  private final List<AlgoritmoAsignacion> algoritmos;

  public MatchmakingController(List<AlgoritmoAsignacion> algoritmos) {
    this.algoritmos = algoritmos;
  }

  public Resultados obtenerRanking(Context ctx){
    Long idSegmentada = ctx.pathParamAsClass("id", Long.class).get();
    DonacionSegmentada donacion = DonacionesRepository.Instance.findSegmentadaById(idSegmentada);
    if(!donacion.estaAlmacen()){
      throw new NotFoundResponse("La donación no se encuentra EN_DEPOSITO");
    }
    if(donacion == null){
      throw new NotFoundResponse("DonacionSegmentada no encontrada");
    };

    List<EntidadBeneficiaria> entidades = EntidadRepository.Instance.obtenerEntidades();

    try{
      return donacion.buscarCandidatas(entidades , algoritmos);

    }catch(IllegalStateException e){
      throw  new NotFoundResponse(e.getMessage());
    }

  }


  public DonacionSegmentada asignarDonacion(Context ctx) {

    AsignarDonacionDTO dto = ctx.bodyAsClass(AsignarDonacionDTO.class);

    Long idDonacion = dto.getDonacionSegmentadaId();
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

  public Map<String, Object> procesarPendientes(Context ctx){
    List<DonacionSegmentada> enDeposito = DonacionesRepository.Instance.findSegmentadasEnDeposito();
    List<EntidadBeneficiaria> entidades = EntidadRepository.Instance.obtenerEntidades();

    int procesadas = 0;

    for(DonacionSegmentada donacion : enDeposito){
      try{
        donacion.buscarCandidatas(entidades , algoritmos);
        procesadas++;
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    }

    return Map.of("Procesadas",procesadas,"total",enDeposito.size());
  }

  private void validarTokenDeJob(Context ctx){
    String tokenEsperado = System.getenv().getOrDefault("MATCHMAKING_JOB_TOKEN","dev_secret_local");
    String tokenRecibido = ctx.header("X-Job-Token");

    if(!tokenEsperado.equals(tokenRecibido)){
      throw  new BadRequestResponse("No autorizado");
    }
  }


}
