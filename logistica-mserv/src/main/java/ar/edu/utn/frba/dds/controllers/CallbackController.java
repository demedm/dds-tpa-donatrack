package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.repositories.RutaRepositorio;
import ar.edu.utn.frba.dds.scripts.dto.DonacionDto;
import ar.edu.utn.frba.dds.scripts.dto.RequestPlanificacionDto;
import ar.edu.utn.frba.dds.scripts.dto.ResponsePlanificacionDto;
import io.javalin.http.Context;
import java.util.ArrayList;
import java.util.List;

public class CallbackController {
  List<RequestPlanificacionDto> donacionesAplanificar = new ArrayList<>();

  public void recibirDonacion(Context ctx) {
    int idDonacion = Integer.parseInt(ctx.pathParam("id"));
    DonacionDto donacion = ctx.bodyAsClass(DonacionDto.class);

    RequestPlanificacionDto replanificar = new RequestPlanificacionDto();
    replanificar.setIdDonacion(idDonacion);
    replanificar.setDireccion(donacion.getDireccionEntidad());
    replanificar.setIdEntidad(donacion.getIdEntidadAsignada());
    if (donacion.getFechaVencimiento() != null) {
      replanificar.setFechaVencimiento(donacion.getFechaVencimiento());
    }

    donacionesAplanificar.add(replanificar);
    ctx.status(201); // CREATED
    ctx.json(replanificar);
  }

  public void solicitudReplanificacion(Entrega entrega) {
    RequestPlanificacionDto planificar = new RequestPlanificacionDto();
    planificar.setIdDonacion(entrega.getDonacionId());
    planificar.setDireccion(entrega.getDireccion());
    planificar.setIdEntidad(entrega.getDonacionId());
    if (entrega.getFechaVencimiento() != null) {
      planificar.setFechaVencimiento(entrega.getFechaVencimiento());
    }
  }

  public void recibirPlanificacion(Context ctx) {
    ResponsePlanificacionDto respuesta = ctx.bodyAsClass(ResponsePlanificacionDto.class);

    List<Ruta> nuevasRutas = new ArrayList<>();
    respuesta.getRutasPlanificadas().forEach(rutaPlanificada -> {
      var patente = rutaPlanificada.getPatenteCamion();
      var entregas = rutaPlanificada.getDestinos().stream().map(destino ->
          new Entrega(destino.getDireccion(), destino.getDonacionId())).toList();
      var ruta = new Ruta(patente, entregas);
      nuevasRutas.add(ruta);
    });

    // por ahora, esta es la replanificacion, no se esta notificando de que no se realizo la entrega
    donacionesAplanificar.addAll(respuesta.getDonacionesNoPlanificadas());

    RutaRepositorio.Instance.addRutasPlanificadas(nuevasRutas);
    ctx.status(200); // OK
  }

}
