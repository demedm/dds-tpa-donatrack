package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.DonacionsDTO;
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

  private ServicioMatchmaking Instance = new ServicioMatchmaking();

  public Resultados obtenerRanking(Context ctx){
    String idSegmentada = ctx.pathParam("id");

    DonacionSegmentada donacion = DonacionesRepository.Instance.findSegmentadaById(idSegmentada);

    if(donacion == null){
      throw new NotFoundResponse("DonacionSegmentada no encontrada");
    };

    List<EntidadBeneficiaria> entidades = EntidadRepository.Instance.obtenerTodas();

    Resultados resultados = ServicioMatchmaking.ejecutar(donacion, entidades);


    return resultados.entidadesPropuestas();


  }

}
