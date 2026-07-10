package ar.edu.utn.frba.dds.model.donantes;

import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;
import ar.edu.utn.frba.dds.model.medioscontacto.Telefono;

public class Persona {
  private String nombreIdentificador; // nombre completo - razon social
  private Mail mail;
  private Telefono telefono;
  private Identificacion identificacion;
  private TipoPersona tipoPersona;
  MedioContacto medioPreferido;

  public Persona() {
  }

  public void setNombreIdentificador(String nombre) {
    this.nombreIdentificador = nombre;
  }

  public void setIdentificacion(Identificacion identificacion) {
    this.identificacion = identificacion;
  }

  public void setMail(Mail mail) {
    this.mail = mail;
  }

  public void setMedioPreferido(MedioContacto medioPreferido) {
    this.medioPreferido = medioPreferido;
  }

  public MedioContacto getMedioPreferido() {
    return this.medioPreferido;
  }

  public void setTelefono(Telefono telefono) {
    this.telefono = telefono;
  }

  public Mail getMail() {
    return mail;
  }
  public String getNombreIdentificador() {
    return nombreIdentificador;
  }

  public String getNroIdentificacion() {
    return identificacion.getNroDocumento();
  }

  public void actualizarInfo(Persona nuevosDatos) {
    this.nombreIdentificador = nuevosDatos.getNombreIdentificador();
    this.identificacion.setNroDocumento(nuevosDatos.getNroIdentificacion());
    this.mail = nuevosDatos.getMail();
    this.telefono = nuevosDatos.getTelefono();
  }

  public void actualizarDatosComunes(String nombreIdentificador, String nroDocumento,
      String email, String telefono) {
    if (nombreIdentificador != null) {
      this.nombreIdentificador = nombreIdentificador;
    }
    if (nroDocumento != null) {
      this.identificacion.setNroDocumento(nroDocumento);
    }
    if (email != null) {
      this.mail = new Mail(email);
    }
    if (telefono != null) {
      this.telefono = new Telefono(telefono);
    }
  }

  public Telefono getTelefono() {
    return telefono;
  }
}
