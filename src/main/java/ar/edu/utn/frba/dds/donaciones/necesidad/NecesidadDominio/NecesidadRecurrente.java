package ar.edu.utn.frba.dds.donaciones.necesidad.NecesidadDominio;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class NecesidadRecurrente extends Necesidad {
  private int periodicidadDias;//Cada cuanto se vence
  public LocalDate proximoVencimiento;

  public NecesidadRecurrente(String entidadId,String descripcion, int periodicidadDias) {
    super(entidadId, descripcion);  // ← LLAMAR A super()
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