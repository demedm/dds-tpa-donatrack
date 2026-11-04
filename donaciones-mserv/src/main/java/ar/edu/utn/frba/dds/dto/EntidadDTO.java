package ar.edu.utn.frba.dds.dto;

import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.medioscontacto.Telefono;

import java.util.ArrayList;
import java.util.List;

public class EntidadDTO {

  public Long id;
  public String direccion;
  public String tipo;
  public List<String> mails;
  public String telefono;

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

  public EntidadBeneficiaria Entidad() {
    List<Mail> mailsContacto = new ArrayList<>();

    if (this.mails != null) {
      mails.forEach(mail -> mailsContacto.add(new Mail(mail)));
    }

    Telefono tel = telefono != null ? new Telefono(telefono) : null;
    return new EntidadBeneficiaria(direccion, mailsContacto, razonSocial, tel, tipo);

  }

  // De la entidad del dominio al JSON que se responde (sin necesidades ni donaciones)
  public static EntidadDTO desde(EntidadBeneficiaria entidad) {
    EntidadDTO dto = new EntidadDTO();
    dto.id = entidad.getId();
    dto.razonSocial = entidad.getRazonSocial();
    dto.direccion = entidad.getDireccion();
    dto.tipo = entidad.getTipoEntidad();
    dto.mails = entidad.getMailsContacto().stream().map(Mail::getMedioContacto).toList();
    dto.telefono = entidad.getTelefono() != null ? entidad.getTelefono().getMedioContacto() : null;
    return dto;
  }

}
