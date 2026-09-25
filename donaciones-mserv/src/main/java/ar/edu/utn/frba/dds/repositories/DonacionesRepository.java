package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.dto.CambioEstadoDTO;
import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class DonacionesRepository implements WithSimplePersistenceUnit {

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
