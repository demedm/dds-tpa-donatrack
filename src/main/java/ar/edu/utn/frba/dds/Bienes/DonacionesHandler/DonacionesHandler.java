package ar.edu.utn.frba.dds.Bienes.DonacionesHandler;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.Notificador;
import ar.edu.utn.frba.dds.Bienes.Donacion;
import ar.edu.utn.frba.dds.Bienes.DonacionSegmentada;
import ar.edu.utn.frba.dds.Bienes.DonacionesDominio.DonacionesRepository;
import ar.edu.utn.frba.dds.Estado.AsignacionRealizada;
import ar.edu.utn.frba.dds.entidad.EntidadFunciones;
import ar.edu.utn.frba.dds.necesidad.NecesidadDominio.Necesidad;
import ar.edu.utn.frba.dds.necesidad.NecesidadDominio.NecesidadRepository;
import ar.edu.utn.frba.dds.necesidad.NecesidadDominio.Peticion;

public class DonacionesHandler {
    private DonacionesRepository donacionesRepository;
    private EntidadFunciones entidadRepository;
    private NecesidadRepository necesidadRepository;

    public DonacionesHandler(DonacionesRepository donacionesRepository, EntidadFunciones entidadRepository, NecesidadRepository necesidadRepository) {
        this.donacionesRepository = donacionesRepository;
        this.entidadRepository = entidadRepository;
        this.necesidadRepository = necesidadRepository;
    }

    public void asignarDonacion(String idDonacion,String idNecesidad){
        Necesidad necesidad = this.necesidadRepository.obtenerPorId(idNecesidad);

        if(necesidad == null){
            throw new IllegalArgumentException("La necesidad ingresada no existe");
        }

        Donacion donacion = this.donacionesRepository.obtenerPorId(idDonacion);

        if(donacion == null){
            throw new IllegalArgumentException("La donacion ingresada no existe");
        }
        //Empiezaa a matchear apra cada una de las donaciones segmentadas, donarlas si es que las pide alguna de las peticiones en la necesidad
        for(Peticion peticion : necesidad.getPeticiones()){
            if(peticion.estaCubierta()){
                continue;
            }//Omite si ay esta cubierta

            String subcategoriaRequerida = peticion.getSubclase();
            Integer cantidadNecesitada = peticion.getCantidadNecesitada();
            List<DonacionSegmentada> donacionesCopia = new ArrayList<>(donacion.getDonaciones());  // ← COPIA


            for(DonacionSegmentada donacionSegmentada : donacionesCopia){
                if(!donacionSegmentada.estaAlmacen()||donacionSegmentada.getCantidad()==0){
                    continue;
                }
                if(!donacionSegmentada.getSubcategoria().equals(subcategoriaRequerida)){
                    continue;
                }

                //Si paso los filtros, me fijo cuanto donar 

                Integer cantidadAAsignar = Math.min(
                    donacionSegmentada.getCantidad(),cantidadNecesitada
                );

                //Creo una nueva con estado asignado

                DonacionSegmentada donacionAsignada = new DonacionSegmentada(cantidadAAsignar, donacionSegmentada.getSubcategoria(),donacionSegmentada.getBienFiltrado());

                donacionAsignada.setEstado(new AsignacionRealizada());
            
                //Agrego esa nueva a las donaciones 
                donacion.agregarDonaciones(donacionAsignada);

                donacionSegmentada.setCantidad(donacionSegmentada.getCantidad()-cantidadAAsignar);
            
                //Actualizo el repositorio con al donacion 

                this.donacionesRepository.actualizar(donacion);

                //Actualizo la peticion, guardando tambien el id de que donacion tome la cantidad en caso de que necesite volver a llevarla al almacen

                peticion.agregarCantidadRecibida(cantidadAAsignar,idDonacion);
                cantidadNecesitada -= cantidadAAsignar;

                new Notificador().enviarNotificacionA(donacion.getDonante(), "Su donacion fue asignada");

                //Si ya la cubri, entonces dejo el bucle

                if(cantidadNecesitada<=0){
                    break;
                }
            }
            if(cantidadNecesitada<=0){
                break;
                }
        }

        this.necesidadRepository.actualizar(necesidad);
        necesidad.actualizarEstado();
        this.necesidadRepository.actualizar(necesidad);
    }
}
