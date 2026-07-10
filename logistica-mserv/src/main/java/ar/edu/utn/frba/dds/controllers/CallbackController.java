package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.Entrega;import ar.edu.utn.frba.dds.scripts.dto.DonacionDTO;

import ar.edu.utn.frba.dds.scripts.dto.PlanificacionDTO;import io.javalin.http.Context;
import java.util.ArrayList;
import java.util.List;

public class CallbackController {
  List<PlanificacionDTO> donacionesAPlanificar = new ArrayList<>();

  public void recibirDonaciones(Context ctx) {
    int idDonacion = Integer.parseInt(ctx.pathParam("id"));
    DonacionDTO donacion = ctx.bodyAsClass(DonacionDTO.class);

    PlanificacionDTO aPlanificar = new PlanificacionDTO();
    aPlanificar.setIdDonacion(idDonacion);
    aPlanificar.setDireccion(donacion.getDireccionEntidad());
    aPlanificar.setIdEntidad(donacion.getIdEntidadAsignada());

    donacionesAPlanificar.add(aPlanificar);
    ctx.status(201); // CREATED
    ctx.json(aPlanificar);
  }

}
