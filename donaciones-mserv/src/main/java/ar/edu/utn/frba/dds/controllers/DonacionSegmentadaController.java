package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.CambioEstadoDTO;
import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.Estado.AsignacionRealizada;
import ar.edu.utn.frba.dds.model.necesidad.Necesidad;
import ar.edu.utn.frba.dds.model.necesidad.Peticion;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;
import ar.edu.utn.frba.dds.repositories.NecesidadRepository;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.util.ArrayList;
import java.util.List;

public class DonacionSegmentadaController {

  public DonacionSegmentada cambiarEstadoDonacion(Context ctx) {
    String idDonacion = ctx.pathParam("id");

    CambioEstadoDTO cambioEstado = ctx.bodyAsClass(CambioEstadoDTO.class);
    DonacionSegmentada donacion = DonacionesRepository.Instance.findSegmentadaById(idDonacion);

    if(donacion == null){
      throw new NotFoundResponse("DonacionSegmentada no encontrada");
    }

    if(cambioEstado.getMotivoFallaEntrega() == null) {
      donacion.cambiarEstado(cambioEstado.getNuevoEstado());
    } else {
      donacion.cambiarEstado(cambioEstado.getNuevoEstado());
      donacion.setJustificacionFallo(cambioEstado.getMotivoFallaEntrega());
    }
    return donacion;
  }

  public DonacionSegmentada asignarDonacion(Context ctx) {
    String idDonacion = ctx.pathParam("id");
    String idEntidad = ctx.pathParam("idEntidad");

    DonacionSegmentada donacion = DonacionesRepository.Instance.findSegmentadaById(idDonacion);

    if(donacion == null){
      throw new NotFoundResponse("DonacionSegmentada no encontrada");
    }

    donacion.asignar();
    donacion.setEntidadAsignadaId(idEntidad);

    return donacion;

  }

//
//
//  public Donacion asignar(Context ctx){
//    String donacionId = ctx.pathParam("idDonacion");
//
//    Donacion donacion = DonacionesRepository.Instance.findById(donacionId);
//
//    if (donacion == null) {
//      throw new NotFoundResponse("Donacion no encontrada");
//    }
//    String necesidadId = ctx.pathParam("idNecesidad");
//    Necesidad necesidad = NecesidadRepository.Instance.findById(necesidadId);
//    if (necesidad == null) {
//      throw new NotFoundResponse("Necesidad no encontrada");
//    }
//    //Empieza a matchear
//
//    for(Peticion peticion : necesidad.getPeticiones()){
//      if(peticion.estaCubierta()){
//        continue;
//      }//Omite si ay esta cubierta
//
//      String subcategoriaRequerida = peticion.getSubclase();
//      Integer cantidadNecesitada = peticion.getCantidadRequerida();
//      List<DonacionSegmentada> donacionesCopia = new ArrayList<>(donacion.getDonaciones());
//      for(DonacionSegmentada donacionSegmentada : donacionesCopia){
//        if(!donacionSegmentada.estaAlmacen()||donacionSegmentada.getCantidad()==0){
//          continue;
//        }
//        if(!donacionSegmentada.getSubcategoria().getDescripcion().equals(subcategoriaRequerida)){
//          continue;
//        }
//        //Si paso los filtros, me fijo cuanto donar
//        Integer cantidadAAsignar = Math.min(
//            donacionSegmentada.getCantidad(),cantidadNecesitada
//        );
//        //Creo una nueva con estado asignado
//
//        DonacionSegmentada donacionAsignada = new DonacionSegmentada(cantidadAAsignar, donacionSegmentada.getSubcategoria(),donacionSegmentada.getBien());
//
//        donacionAsignada.setEstado(new AsignacionRealizada());
//
//        //Agrego esa nueva a las donaciones
//        donacion.agregarDonaciones(donacionAsignada);
//
//        donacionSegmentada.setCantidad(donacionSegmentada.getCantidad()-cantidadAAsignar);
//
//        //Actualizo el repositorio con al donacion
//
//        DonacionesRepository.Instance.actualizar(donacion);
//
//        //Actualizo la peticion, guardando tambien el id de que donacion tome la cantidad en caso de que necesite volver a llevarla al almacen
//
//        peticion.agregarCantidadRecibida(cantidadAAsignar, String.valueOf(donacionId));
//        cantidadNecesitada -= cantidadAAsignar;
//
//        // new Notificador().enviarNotificacionA(donacion.getDonante(), "Su donacion fue asignada");
//
//        //Si ya la cubri, entonces dejo el bucle
//        if(cantidadNecesitada<=0){
//          break;
//        }
//      }
//      if(cantidadNecesitada<=0){
//        break;
//      }
//    }
//    return DonacionesRepository.Instance.obtenerPorId(donacionId);
//  }


}
