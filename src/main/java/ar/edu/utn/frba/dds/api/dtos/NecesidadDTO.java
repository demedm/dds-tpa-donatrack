package ar.edu.utn.frba.dds.api.dtos;

import ar.edu.utn.frba.dds.api.repository.RegistroEntidades;
import ar.edu.utn.frba.dds.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.necesidad.NecesidadDominio.Necesidad;
import ar.edu.utn.frba.dds.necesidad.NecesidadDominio.Peticion;

import java.util.List;

public class NecesidadDTO {
  public String descripcion;
  public String razonSocialEntidad;
  public String estado;
  public List<Peticion> peticiones;

  public Necesidad convertirDtoAObjeto() {
    EntidadBeneficiaria entidad =RegistroEntidades.buscarPorRazonSocial(razonSocialEntidad)
        .orElseThrow(() -> new RuntimeException("No se encontró entidad: " + razonSocialEntidad));

    Necesidad necesidad =new Necesidad(entidad, descripcion);

    if (peticiones != null) {
      peticiones.forEach(necesidad::agregarPeticion);
    }

    return necesidad;
  }

}
