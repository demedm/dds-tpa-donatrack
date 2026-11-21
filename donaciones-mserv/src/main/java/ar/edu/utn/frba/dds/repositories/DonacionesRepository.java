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
        if (donacion.getId() == null){
            entityManager().persist(donacion);
        }else{
            entityManager().merge(donacion);
        }
    }

    public Donacion findById(Long id){
        return entityManager().find(Donacion.class, id);
    }

    public List<Donacion> obtenerTodas(){
        return entityManager().createQuery("from Donacion", Donacion.class).getResultList();
    }
    public void actualizarSegmentada(DonacionSegmentada segmentada) {
        entityManager().merge(segmentada);
    }
    public void eliminar(Long id){
        Donacion donacion = entityManager().find(Donacion.class, id);
        if (donacion != null){
            entityManager().remove(donacion);
        }
    }

    //Donaciones segmentadas

    public DonacionSegmentada findSegmentadaById(Long segmentadaId) {

        return entityManager().find(DonacionSegmentada.class, segmentadaId);

    }
   
    //Para los algoritmos

    public List<DonacionSegmentada> findSegmentadasEnDeposito(){
        return entityManager()
            .createQuery("from DonacionSegmentada ds where ds.estadoActual = :estado or ds.estadoActual is null",
                DonacionSegmentada.class)
            .setParameter("estado", new EnDeposito())
            .getResultList();
    }

    public List<Donacion> findDeDonante(Long donanteId){
        return entityManager()
            .createQuery("from Donacion d where d.donante.id = :donanteId order by d.fechaRecepcion desc, d.id desc", Donacion.class)
            .setParameter("donanteId",donanteId)
            .getResultList();
    }

    //pantala 9

    public List<DonacionSegmentada> findAsignadaA(Long entidadId){
        return entityManager()
            .createQuery("from DonacionSegmentada  s where s.entidadAsignadaId = :entidadId " + "order by s.donacion.fechaRecepcion desc, s.id desc", DonacionSegmentada.class)
            .setParameter("entidadId",entidadId)
            .getResultList();
    }


}
