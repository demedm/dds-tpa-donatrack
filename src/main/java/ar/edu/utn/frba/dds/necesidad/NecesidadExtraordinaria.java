package ar.edu.utn.frba.dds.necesidad;

import ar.edu.utn.frba.dds.necesidad.NecesidadDominio.Necesidad;
import ar.edu.utn.frba.dds.entidad.EntidadBeneficiaria;

public class NecesidadExtraordinaria extends Necesidad {
  public NecesidadExtraordinaria(EntidadBeneficiaria entidad, String descripcion) {
    super(entidad, descripcion);
  }
}
