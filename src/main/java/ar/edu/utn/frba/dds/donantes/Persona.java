package ar.edu.utn.frba.dds.donantes;

import ar.edu.utn.frba.dds.medioscontacto.Mail;
import ar.edu.utn.frba.dds.medioscontacto.MedioContacto;
import ar.edu.utn.frba.dds.medioscontacto.Telefono;

import java.util.Objects;

public class Persona {
  private String nombreIdentificador; // nombre completo - razon social
  private Mail mail;
  private Telefono telefono;
  private Identificacion identificacion;
  private TipoPersona tipoPersona;

  public Persona(String nombre, Mail mail, Identificacion identificacion, TipoPersona tipo) {
    this.nombreIdentificador = nombre;
    this.mail = mail;
    this.identificacion = identificacion;
    this.tipoPersona = tipo;
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

}
