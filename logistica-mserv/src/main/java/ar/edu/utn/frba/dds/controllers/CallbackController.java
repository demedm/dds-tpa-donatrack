package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.grpc.RequestPlanificacionDto;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.planificacion.ResultadoPlanificacion;
import ar.edu.utn.frba.dds.planificacion.tareas.PlanificarDonaciones;
import ar.edu.utn.frba.dds.repositories.RutaRepositorio;
import ar.edu.utn.frba.dds.repositories.planificacion.PlanificacionRepositorio;
import ar.edu.utn.frba.dds.scripts.dto.DonacionDto;
import ar.edu.utn.frba.dds.scripts.dto.planificacion.ResponsePlanificacionDto;
import ar.edu.utn.frba.dds.scripts.dto.planificacion.SolicitudPlanificacionDto;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import  ar.edu.utn.frba.dds.scripts.dto.DonacionSegmentadaDTO;
import ar.edu.utn.frba.dds.scripts.dto.planificacion.RutaPlanificadaDto;
import ar.edu.utn.frba.dds.scripts.dto.planificacion.DestinoDto;
import ar.edu.utn.frba.dds.main.HttpClient.ClientDonaciones;
import io.javalin.http.NotFoundResponse;

public class CallbackController {
  List<RequestPlanificacionDto> donacionesAplanificar = new ArrayList<>();
  private ClientDonaciones clientDonaciones = new ClientDonaciones();

  public void recibirPlanificacion(Context ctx) {

    ResponsePlanificacionDto resp;

    try {
      resp = ctx.bodyAsClass(ResponsePlanificacionDto.class);
    } catch (Exception e) {
      throw new BadRequestResponse("JSON inválido");
    }
    ResultadoPlanificacion r;

    try {
      r = PlanificacionRepositorio.Instance.procesarRespuesta(resp);
    } catch (NoSuchElementException e) {
      throw new NotFoundResponse(e.getMessage());
    } catch (IllegalArgumentException e) {
      throw new BadRequestResponse(e.getMessage());
    }

    if (!r.yaProcesado) {
      // 1. Notificar a Donaciones el resultado confirmado.
      try {
        notificarResultado(r);
      } catch (Exception e) {
        System.err.println("Error notificando el resultado a Donaciones: " + e.getMessage());
      }
      try {
        PlanificarDonaciones.ejecutar();
      } catch (Exception e) {
        System.err.println("Error iniciando la siguiente planificación: " + e.getMessage());
      }
    }
    ctx.status(200);
  }

  private void notificarResultado(ResultadoPlanificacion r) {
    r.asignadas.forEach(id -> clientDonaciones.notificarCambioEstado(id, "LISTA_PARA_ENTREGAR"));
    r.noPlanificables.forEach(id ->
        clientDonaciones.notificarFallaDeEntrega(id, "Sin camión disponible", false));
  }

  /*
  public void recibirDonacion(Context ctx) {
    System.out.println("¡Llegó la donación ID: " + ctx.pathParam("id") + " desde el otro microservicio!");

    Long idDonacion = Long.parseLong(ctx.pathParam("id"));
    DonacionDto donacion = ctx.bodyAsClass(DonacionDto.class);

    RequestPlanificacionDto replanificar = new SolicitudPlanificacionDto();
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

  public RequestPlanificacionDto solicitudReplanificacion(Entrega entrega) {
    RequestPlanificacionDto planificar = new RequestPlanificacionDto();
    planificar.setIdDonacion(entrega.getDonacionId());
    planificar.setDireccion(entrega.getDireccion());
    planificar.setIdEntidad(entrega.getDonacionId());
    if (entrega.getFechaVencimiento() != null) {
      planificar.setFechaVencimiento(entrega.getFechaVencimiento());
    }
    return planificar;
  }
  public void recibirPlanificacion(Context ctx) {
    ResponsePlanificacionDto respuesta = ctx.bodyAsClass(ResponsePlanificacionDto.class);

    // Instanciamos nuestro nuevo cliente
    ClientDonaciones cliente = new ClientDonaciones();

    List<Ruta> rutasGuardadas = new ArrayList<>();

    if (respuesta.getRutasPlanificadas() != null) {
      for (RutaPlanificadaDto rutaDto : respuesta.getRutasPlanificadas()) {
        List<Entrega> entregas = new ArrayList<>();

        for (DestinoDto destino : rutaDto.getDestinos()) {
          Entrega nuevaEntrega = new Entrega(destino.getDireccion(), destino.getDonacionId());
          entregas.add(nuevaEntrega);

          // Usamos el cliente para avisar el éxito
          cliente.notificarCambioEstado(destino.getDonacionId(), "LISTA_PARA_ENTREGAR");
        }

        Ruta nuevaRuta = new Ruta(entregas);
        if (rutaDto.getCamion() != null) {
          nuevaRuta.asignarCamion(rutaDto.getCamion());
        }
        rutasGuardadas.add(nuevaRuta);
      }
    }

    RutaRepositorio.Instance.recibirRespuestaPlanificacion(respuesta);

    if (respuesta.getDonacionesNoPlanificadas() != null) {
      for (RequestPlanificacionDto noAsignada : respuesta.getDonacionesNoPlanificadas()) {
        //  cliente para avisar el fallo
        cliente.notificarFallaDeEntrega(noAsignada.getIdDonacion(), "Sin espacio en ruta", true);
      }
    }
    ctx.status(200);
  }
  public void recibirLotePlanificacion(Context ctx) {
    // Jackson convierte el arreglo JSON en una lista de DTOs automáticamente
    DonacionSegmentadaDTO[] loteRecibido = ctx.bodyAsClass(DonacionSegmentadaDTO[].class);

    System.out.println("¡Llegó un lote para planificar con " + loteRecibido.length + " donaciones!");
    ctx.status(200).result("Lote recibido");
  }
   */
}
