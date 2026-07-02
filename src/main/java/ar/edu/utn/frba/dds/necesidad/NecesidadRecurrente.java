package ar.edu.utn.frba.dds.necesidad;

import ar.edu.utn.frba.dds.EntidadBeneficiaria;

public class NecesidadRecurrente extends Necesidad {
  public int diasAvencer;

public NecesidadRecurrente(EntidadBeneficiaria entidad, int diasAvencer) {
    super(entidad);
    this.diasAvencer = diasAvencer;
  }

  public int getDiasAvencer() {
    return diasAvencer;
  }
}
