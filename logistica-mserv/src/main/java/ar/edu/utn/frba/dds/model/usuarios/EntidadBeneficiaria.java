package ar.edu.utn.frba.dds.model.usuarios;

import ar.edu.utn.frba.dds.model.Entrega;

import java.util.List;

public class EntidadBeneficiaria {
  private String contacto;
  private String id;
  private String direccion;
  private List<Entrega> entregasAsignadas;

  public EntidadBeneficiaria() {}

  public void confirmarEntrega(String idEntrega, String URLfoto) {
    entregasAsignadas.stream().filter(entrega -> entrega.getId().equals(idEntrega))
        .forEach(entrega -> entrega.confirmarEntrega(URLfoto));
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
