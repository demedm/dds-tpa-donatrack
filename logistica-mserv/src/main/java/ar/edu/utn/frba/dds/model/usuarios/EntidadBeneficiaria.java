package ar.edu.utn.frba.dds.model.usuarios;

import ar.edu.utn.frba.dds.model.Entrega;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("EB")
public class EntidadBeneficiaria extends Usuario {
  private String razonSocial;
  private String direccion;

  public EntidadBeneficiaria() {}

  public EntidadBeneficiaria(String email, String direccion, String razonSocial) {
    super(email);
    this.direccion = direccion;
    this.razonSocial = razonSocial;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public String getRazonSocial() {
    return razonSocial;
  }
}
