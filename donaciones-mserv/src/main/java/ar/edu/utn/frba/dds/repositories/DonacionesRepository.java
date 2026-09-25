package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.Estado.EnDeposito;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;

public class DonacionesRepository implements WithSimplePersistenceUnit {

    public static final DonacionesRepository Instance = new DonacionesRepository();


    //Donaciones

    public void guardar(Donacion donacion) {
        withTransaction(() -> {
            if (entityManager().find(Donacion.class, donacion.getId()) == null){
                entityManager().persist(donacion);
            }else{
                entityManager().merge(donacion);
            }
        });

    }

    public Donacion findById(String id){
        return entityManager().find(Donacion.class, id);
    }

    public List<Donacion> obtenerTodas(){
        return entityManager().createQuery("from Donacion", Donacion.class).getResultList();
    }

    public void eliminar(String id){
        withTransaction(() -> {
            Donacion donacion = entityManager().find(Donacion.class, id);
            if (donacion != null){
                entityManager().remove(donacion);
            }
        });
    }

    //Donaciones segmentadas

    public DonacionSegmentada findSegmentadaById(String segmentadaId) {

        return entityManager().find(DonacionSegmentada.class, segmentadaId);

    }

    //Para los algoritmos

    public List<DonacionSegmentada> findSegmentadasEnDeposito(){
        return entityManager()
            .createQuery("from DonacionSegmentada ds where ds.estadoActual = :estado", DonacionSegmentada.class)
            .setParameter("estado", new EnDeposito())
            .getResultList();
    }

}
