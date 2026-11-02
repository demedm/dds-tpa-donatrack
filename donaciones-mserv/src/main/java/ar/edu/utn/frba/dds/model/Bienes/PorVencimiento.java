package ar.edu.utn.frba.dds.model.Bienes;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@DiscriminatorValue("VENCIMIENTO")

public class PorVencimiento extends CriterioSegmentacion{

  @Column(name = "fecha_vencimiento")
  private LocalDate fechaVencimiento;

  protected PorVencimiento() {}

  public PorVencimiento(LocalDate fechaVencimiento) {
    if (fechaVencimiento == null) {
      throw new IllegalArgumentException("Un bien perecedero necesita fecha de vencimiento");
    }
    this.fechaVencimiento = fechaVencimiento;
  }

  public LocalDate getFechaVencimiento() {
    return fechaVencimiento;
  }

  public boolean vencioAl(LocalDate dia){
    return fechaVencimiento.isBefore(dia);
  }

  @Override
  public Object valor(){
    return fechaVencimiento;
  }


}
