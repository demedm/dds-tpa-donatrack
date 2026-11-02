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
import  ar.edu.utn.frba.dds.scripts.dto.DonacionSegmentadaDTO;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.repositories.RutaRepositorio;
import ar.edu.utn.frba.dds.scripts.dto.ResponsePlanificacionDto;
import ar.edu.utn.frba.dds.scripts.dto.RutaPlanificadaDto;
import ar.edu.utn.frba.dds.scripts.dto.DestinoDto;
import ar.edu.utn.frba.dds.scripts.dto.RequestPlanificacionDto;
import io.javalin.http.Context;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import ar.edu.utn.frba.dds.main.ClientDonaciones;

public class CallbackController {
  List<RequestPlanificacionDto> donacionesAplanificar = new ArrayList<>();

  public void recibirDonacion(Context ctx) {
    System.out.println("¡Llegó la donación ID: " + ctx.pathParam("id") + " desde el otro microservicio!");

    Long idDonacion = Long.parseLong(ctx.pathParam("id"));
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
}
