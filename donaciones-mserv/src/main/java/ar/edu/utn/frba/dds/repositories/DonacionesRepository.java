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
            if (donacion.getId() == null){
                entityManager().persist(donacion);
            }else{
                entityManager().merge(donacion);
            }
        });

    }

    public Donacion findById(Long id){
        return entityManager().find(Donacion.class, id);
    }

    public List<Donacion> obtenerTodas(){
        return entityManager().createQuery("from Donacion", Donacion.class).getResultList();
    }

    public void eliminar(Long id){
        withTransaction(() -> {
            Donacion donacion = entityManager().find(Donacion.class, id);
            if (donacion != null){
                entityManager().remove(donacion);
            }
        });
    }

    //Donaciones segmentadas

    public DonacionSegmentada findSegmentadaById(Long segmentadaId) {

        return entityManager().find(DonacionSegmentada.class, segmentadaId);

    }

    //Para los algoritmos

    public List<DonacionSegmentada> findSegmentadasEnDeposito(){

        return entityManager()
            .createQuery("from DonacionSegmentada ", DonacionSegmentada.class)
            .getResultList().stream()
            .filter(DonacionSegmentada::estaAlmacen)
            .toList();

        /*
        return entityManager()
            .createQuery("from DonacionSegmentada ds where ds.estadoActual = :estado", DonacionSegmentada.class)
            .setParameter("estado", new EnDeposito())
            .getResultList();

         */
    }

}
