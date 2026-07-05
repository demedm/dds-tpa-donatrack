package ar.edu.utn.frba.dds.Bienes.DonacionesDominio;

import ar.edu.utn.frba.dds.Bienes.Bien;
import ar.edu.utn.frba.dds.Bienes.Donacion;
import ar.edu.utn.frba.dds.necesidad.NecesidadDominio.Necesidad;
import ar.edu.utn.frba.dds.Bienes.DonacionSegmentada;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DonacionesRepository {
    public List<Donacion> donaciones = new ArrayList<>();

    public void agregarDonacion(Donacion donacion){
        donaciones.add(donacion);
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

    public Donacion obtenerPorId(String id){
        return this.donaciones.stream()
        .filter(d->d.getId().equals(id))
        .findFirst()
        .orElse(null);
    }

  //Obtener todas las donaciones  

    public List<Donacion> obtenerTodas(){
        return new ArrayList<>(this.donaciones);
    }

    public void eliminar(String id){
        this.donaciones.removeIf(d->d.getId().equals(id));
    }

    public void actualizar(Donacion donacion){
        Donacion existente = obtenerPorId(donacion.getId());

        existente.setDonacionesSegmentadas(donacion.getDonaciones());    }
    }

