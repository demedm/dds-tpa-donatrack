package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.dto.CambioEstadoDTO;
import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class DonacionesRepository {
    public static DonacionesRepository Instance = new DonacionesRepository();
    public List<Donacion> donaciones = new ArrayList<>();

    //Donaciones

    public Donacion guardar(Donacion donacion) {

        Donacion existente = findById(donacion.getId());

        if(existente == null){
            donaciones.add(donacion);
        } else {
            existente.setDescripcionGeneral(donacion.getDescripcionGeneral());
        }

        return existente;
    }

    public Donacion findById(String id){
        return this.donaciones.stream()
            .filter(d-> d.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    public List<Donacion> obtenerTodas(){
        return new ArrayList<>(this.donaciones);
    }

    public void eliminar(String id){
        this.donaciones.removeIf(d->d.getId().equals(id));
    }

    //Donaciones segmentadas

    public DonacionSegmentada findSegmentadaById(String segmentadaId) {
        return this.donaciones.stream()
            .flatMap(d -> d.getDonaciones().stream())
            .filter(ds -> ds.getId().equals(segmentadaId))
            .findFirst()
            .orElse(null);
    }


    //Para los algoritmos

    public List<DonacionSegmentada> findSegmentadasEnDeposito(){
        return this.donaciones.stream()
            .flatMap(d->d.getDonaciones().stream())
            .filter(DonacionSegmentada::estaAlmacen)
            .toList();
    }

}


// public  ResultadoBusqueda buscarProducto(String subcategoria, int cantidad){
//     List<Bien> bienesFiltrrados = donaciones.stream()
//         .flatMap(d->d.getBienes().stream())
//         .filter(b->b.getSubcategoria().equals(subcategoria))
//         .collect(Collectors.toList());

//     List<Bien> bienesAsignados = new ArrayList<>();
//     int restante = cantidad;
//     for (Bien bien : bienesFiltrrados) {
//     if (restante == 0) break;
//     int aRestar = Math.min(bien.getCantidad(), restante);
//     bien.copia(aRestar, EstadoDonacion.ASIGNACION_REALIZADA);
//     bien.setCantidad(bien.getCantidad() - aRestar);
//     restante -= aRestar;
//     }
//     return new ResultadoBusqueda(restante,bienesAsignados);
// }