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
  private String contacto;
  private String direccion;

  public EntidadBeneficiaria() {}

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public String getContacto() {
    return contacto;
  }

  public void setContacto(String contacto) {
    this.contacto = contacto;
  }

}
