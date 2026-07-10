package ar.edu.utn.frba.dds.dto;

import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.medioscontacto.Telefono;

import java.util.List;

public class EntidadDTO {

  public String razonSocial;

  public String getRazonSocial() {
    return razonSocial;
  }

  public void setRazonSocial(String razonSocial) {
    this.razonSocial = razonSocial;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public List<String> getMails() {
    return mails;
  }

  public void setMails(List<String> mails) {
    this.mails = mails;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String direccion;
  public String tipo;
  public List<String> mails;
  public String telefono;

  public EntidadBeneficiaria Entidad() {
    List<Mail> mailsContacto = mails.stream().map(Mail::new).toList();
    return new EntidadBeneficiaria(direccion, mailsContacto, razonSocial, new Telefono(telefono), tipo);
  }

}
