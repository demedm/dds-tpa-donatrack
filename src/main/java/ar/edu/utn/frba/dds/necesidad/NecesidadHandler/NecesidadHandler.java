package ar.edu.utn.frba.dds.necesidad.NecesidadHandler;

import ar.edu.utn.frba.dds.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.entidad.EntidadRepository;
import ar.edu.utn.frba.dds.necesidad.NecesidadDominio.*;

public class NecesidadHandler {
    
    private NecesidadRepository necesidadRepository;
    private EntidadRepository entidadRepository;

    public NecesidadHandler(NecesidadRepository necesidadRepository,EntidadRepository entidadRepository) {
        this.necesidadRepository = necesidadRepository;
        this.entidadRepository = entidadRepository;
    }

    public Necesidad crearNecesidad(String entidadId, String descripcion){
    //Validadciones

        if (entidadId==null || entidadId.isEmpty()) {
            throw new IllegalArgumentException("La entidad es obligatoria");
        }

        EntidadBeneficiaria entidad = this.entidadRepository.obtenerPorId(entidadId);

        if (entidad == null) {
        throw new IllegalArgumentException("La entidad ingresada no existe");
        }

        Necesidad necesidad = new Necesidad (entidad,descripcion);

        necesidad.setDependencias(necesidadRepository,entidadRepository);

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