package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.accionesrutas.AccionesSobreRutas;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.repositories.RutaRepositorio;
import ar.edu.utn.frba.dds.scripts.dto.DonacionDTO;

import ar.edu.utn.frba.dds.scripts.dto.RequestPlanificacionDTO;
import ar.edu.utn.frba.dds.scripts.dto.ResponsePlanificacionDTO;
import io.javalin.http.Context;
import java.util.ArrayList;
import java.util.List;

public class CallbackController {
  List<RequestPlanificacionDTO> donacionesAPlanificar = new ArrayList<>();

  public void recibirDonacion(Context ctx) {
    int idDonacion = Integer.parseInt(ctx.pathParam("id"));
    DonacionDTO donacion = ctx.bodyAsClass(DonacionDTO.class);

    RequestPlanificacionDTO aPlanificar = new RequestPlanificacionDTO();
    aPlanificar.setIdDonacion(idDonacion);
    aPlanificar.setDireccion(donacion.getDireccionEntidad());
    aPlanificar.setIdEntidad(donacion.getIdEntidadAsignada());
    if(donacion.getFechaVencimiento() != null) {
      aPlanificar.setFechaVencimiento(donacion.getFechaVencimiento());
    }

    donacionesAPlanificar.add(aPlanificar);
    ctx.status(201); // CREATED
    ctx.json(aPlanificar);
  }

  public void solicitudReplanificacion(Entrega entrega) {
    RequestPlanificacionDTO aPlanificar = new RequestPlanificacionDTO();
    aPlanificar.setIdDonacion(entrega.getDonacionId());
    aPlanificar.setDireccion(entrega.getDireccion());
    aPlanificar.setIdEntidad(entrega.getDonacionId());
    if(entrega.getFechaVencimiento() != null) {
      aPlanificar.setFechaVencimiento(entrega.getFechaVencimiento());
    }
  }

  public void recibirPlanificacion(Context ctx) {
    ResponsePlanificacionDTO respuesta = ctx.bodyAsClass(ResponsePlanificacionDTO.class);

    List<Ruta> nuevasRutas = new ArrayList<>();
    respuesta.getRutasPlanificadas().forEach(rutaPlanificada -> {
      var patente = rutaPlanificada.getPatenteCamion();
      var entregas = rutaPlanificada.getDestinos().stream().map(destino ->
          new Entrega(destino.getDireccion(), destino.getDonacionId())).toList();
      var ruta = new Ruta(patente, entregas);
      nuevasRutas.add(ruta);
    });

    // por ahora, esta es la replanificacion, no se esta notificando de que no se realizo la entrega
    donacionesAPlanificar.addAll(respuesta.getDonacionesNoPlanificadas());

    RutaRepositorio.Instance.addRutasPlanificadas(nuevasRutas);
    ctx.status(200); // OK
  }

}
