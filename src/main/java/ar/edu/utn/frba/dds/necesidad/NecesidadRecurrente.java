package ar.edu.utn.frba.dds.necesidad;

import ar.edu.utn.frba.dds.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.necesidad.NecesidadDominio.Necesidad;

public class NecesidadRecurrente extends Necesidad {
  public int diasAvencer;

public NecesidadRecurrente(EntidadBeneficiaria entidad, int diasAvencer, String descripcion) {
    super(entidad, descripcion);
    this.diasAvencer = diasAvencer;
  }

  public int getDiasAvencer() {
    return diasAvencer;
  }
}
