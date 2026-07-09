package ar.edu.utn.frba.dds.model.necesidad.NecesidadHandler;

import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.necesidad.NecesidadDominio.Necesidad;
import ar.edu.utn.frba.dds.model.necesidad.NecesidadDominio.NecesidadRecurrente;
import ar.edu.utn.frba.dds.model.necesidad.NecesidadDominio.Peticion;
import ar.edu.utn.frba.dds.model.necesidad.NecesidadDominio.*;
import ar.edu.utn.frba.dds.repositories.EntidadRepository;
import ar.edu.utn.frba.dds.repositories.NecesidadRepository;

public class NecesidadHandler {
    
    private NecesidadRepository necesidadRepository;

    public NecesidadHandler(NecesidadRepository necesidadRepository) {
        this.necesidadRepository = necesidadRepository;
    }

    public Necesidad crearNecesidad(String entidadId, String descripcion, Integer diasAVencer){
    //Validadciones

        if (entidadId==null || entidadId.isEmpty()) {
            throw new IllegalArgumentException("La entidad es obligatoria");
        }

        EntidadBeneficiaria entidad = EntidadRepository.Instance.obtenerPorId(entidadId);

        if (entidad == null) {
        throw new IllegalArgumentException("La entidad ingresada no existe");
        }

        Necesidad necesidad;

        if (diasAVencer != null) {
            // Es recurrente
            necesidad = new NecesidadRecurrente(entidadId, descripcion, diasAVencer);
        } else {
            // Es extraordinaria
            necesidad = new Necesidad(entidadId, descripcion);
        }

        return this.necesidadRepository.agregarNecesidad(necesidad);
        }

    public Necesidad agregarPeticiones(String necesidadId,String subcategoria, Integer cantidadRequerida) {
            if (subcategoria == null || subcategoria.isEmpty()){
                throw new IllegalArgumentException("La suibcategoria es obligatoria");
            }
            
            Necesidad necesidad = this.necesidadRepository.obtenerPorId(necesidadId);

            Peticion peticion = new Peticion(subcategoria,cantidadRequerida);

            necesidad.agregarPeticion(peticion);

            return this.necesidadRepository.actualizar(necesidad);
            //Voy aniadiendo a la lista de la necesidad las distintas peticiones de cosas que tengo
    }
}