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
  @Id
  @GeneratedValue
  private Long id1;
  private String contacto;
  private String id;
  private String direccion;

  @OneToMany
  private List<Entrega> entregasAsignadas;

  public EntidadBeneficiaria() {}

  public void confirmarEntrega(String idEntrega, String urlFoto) {
    entregasAsignadas.stream().filter(entrega -> entrega.getId().equals(idEntrega))
        .forEach(entrega -> entrega.confirmarEntrega(urlFoto));
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Long getId1() {
    return id1;
  }

  public void setId1(Long id1) {
    this.id1 = id1;
  }

  public String getContacto() {
    return contacto;
  }

  public void setContacto(String contacto) {
    this.contacto = contacto;
  }

  public void setEntregasAsignadas(List<Entrega> entregasAsignadas) {
    this.entregasAsignadas = entregasAsignadas;
  }

  public List<Entrega> getEntregasAsignadas() {
    return entregasAsignadas;
  }

}
