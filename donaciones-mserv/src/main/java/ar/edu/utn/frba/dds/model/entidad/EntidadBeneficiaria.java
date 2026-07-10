package ar.edu.utn.frba.dds.model.entidad;

import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.medioscontacto.Telefono;
import ar.edu.utn.frba.dds.model.necesidad.Necesidad;

import java.util.ArrayList;
import java.util.List;

public class EntidadBeneficiaria {
  private String razonSocial;
  private String direccion;
  private String tipo;
  private List<Mail> mailsContacto;
  private Telefono telefono;
  private List<DonacionSegmentada> donacionesRecibidas = new ArrayList<>();

  private List<Necesidad> necesidades = new ArrayList<>();

  public EntidadBeneficiaria() {}

  public void setRazonSocial(String razonSocial) {
    this.razonSocial = razonSocial;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public void setTelefono(Telefono telefono) {
    this.telefono = telefono;
  }

  public void setMailsContacto(List<Mail> mailsContacto) {
    this.mailsContacto.addAll(mailsContacto);
  }

  public List<DonacionSegmentada> getDonacionesRecibidas() {
    return this.donacionesRecibidas;
  }

  public String getTipoEntidad() {
    return tipo;
  }

  public String getDireccion() {
    return direccion;
  }

  public List<Mail> getMailsContacto() {
    return this.mailsContacto;
  }

/*
  public Necesidad crearNecesidad(GestorNecesidades gestor) {
    Necesidad necesidad = new Necesidad(this);
    gestor.agregarNecesidad(necesidad);
    return necesidad;
  }
  public Necesidad crearNecesidad(GestorNecesidades gestor){
    Necesidad necesidad = new Necesidad(this);
    gestor.agregarNecesidad(necesidad);
    return necesidad;
  }
*/

  public List<Necesidad> getNecesidades() {
    return necesidades;
  }
}