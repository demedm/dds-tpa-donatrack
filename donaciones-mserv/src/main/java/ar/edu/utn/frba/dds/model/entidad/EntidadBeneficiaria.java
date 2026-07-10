package ar.edu.utn.frba.dds.model.entidad;

import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.medioscontacto.Telefono;
import ar.edu.utn.frba.dds.model.necesidad.Necesidad;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntidadBeneficiaria {
  private String id;
  private String razonSocial;
  private String direccion;
  private String tipo;
  private List<Mail> mailsContacto = new ArrayList<>();
  private Telefono telefono;
  private List<DonacionSegmentada> donacionesRecibidas = new ArrayList<>();

  private List<Necesidad> necesidades = new ArrayList<>();

  public EntidadBeneficiaria() {}

  public EntidadBeneficiaria(String direccion, List<Mail> mails, String razonSocial, Telefono telefono, String tipo) {
    this.direccion = direccion;
    this.mailsContacto = mails;
    this.razonSocial = razonSocial;
    this.telefono = telefono;
    this.tipo = tipo;

  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }
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

  public Telefono getTelefono() {
    return telefono;
  }
  public void setMailsContacto(List<Mail> mailsContacto) {
    this.mailsContacto = mailsContacto;
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