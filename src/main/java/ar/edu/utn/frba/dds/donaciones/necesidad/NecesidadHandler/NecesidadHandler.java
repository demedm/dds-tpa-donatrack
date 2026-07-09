package ar.edu.utn.frba.dds.donaciones.necesidad.NecesidadHandler;

import ar.edu.utn.frba.dds.donaciones.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.donaciones.entidad.EntidadFunciones;
import ar.edu.utn.frba.dds.donaciones.necesidad.NecesidadDominio.*;

public class NecesidadHandler {
    
    private NecesidadRepository necesidadRepository;
    private EntidadFunciones entidadRepository;

    public NecesidadHandler(NecesidadRepository necesidadRepository, EntidadFunciones entidadRepository) {
        this.necesidadRepository = necesidadRepository;
        this.entidadRepository = entidadRepository;
    }

    public Necesidad crearNecesidad(String entidadId, String descripcion, Integer diasAVencer){
    //Validadciones

        if (entidadId==null || entidadId.isEmpty()) {
            throw new IllegalArgumentException("La entidad es obligatoria");
        }

        EntidadBeneficiaria entidad = this.entidadRepository.obtenerPorId(entidadId);

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