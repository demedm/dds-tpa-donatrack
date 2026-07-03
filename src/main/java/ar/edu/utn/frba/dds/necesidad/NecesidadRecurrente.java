package ar.edu.utn.frba.dds.necesidad;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import ar.edu.utn.frba.dds.EntidadBeneficiaria;

public class NecesidadRecurrente extends Necesidad {
  private int periodicidadDias;//Cada cuanto se vence
  public LocalDate proximoVencimiento;

  public NecesidadRecurrente(EntidadBeneficiaria entidad, int periodicidadDias) {
    super(entidad);
    this.periodicidadDias = periodicidadDias;
    this.proximoVencimiento = LocalDate.now().plusDays(periodicidadDias);

  }

  public long getDiasAvencer() {
    return ChronoUnit.DAYS.between(LocalDate.now(), proximoVencimiento);
  }

  public void reiniciarPeriodo(){
    this.proximoVencimiento=LocalDate.now().plusDays(periodicidadDias);
  }
}