package ar.edu.utn.frba.dds.donantes;

import ar.edu.utn.frba.dds.medioscontacto.Mail;
import ar.edu.utn.frba.dds.medioscontacto.MedioContacto;
import ar.edu.utn.frba.dds.medioscontacto.Telefono;

public class Persona {
  private String nombreIdentificador; // nombre completo - razon social
  private Mail mail;
  private Telefono telefono;
  private Identificacion identificacion;
  private TipoPersona tipoPersona;
  MedioContacto medioPreferido;

  public Persona(String nombre, Mail mail, Identificacion identificacion, TipoPersona tipo) {
    this.nombreIdentificador = nombre;
    this.mail = mail;
    this.identificacion = identificacion;
    this.tipoPersona = tipo;
    this.medioPreferido = mail; //por defecto
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

  public Telefono getTelefono() {
    return telefono;
  }

  public String getNombreIdentificador() {
    return nombreIdentificador;
  }

  public String getNroIdentificacion() {
    return identificacion.getNroDocumento();
  }

  public Mail getMail() {
    return mail;
  }

  public void actualizarDatosDonantes(Persona nuevaInfo) {
    if (nuevaInfo.getNombreIdentificador() != null) {
      this.nombreIdentificador = nuevaInfo.getNombreIdentificador();
    }
    if (nuevaInfo.getTelefono() != null) {
      this.telefono = nuevaInfo.getTelefono();
    }
  }
}