package ar.edu.utn.frba.dds.donantes;

import ar.edu.utn.frba.dds.medioscontacto.Mail;
import ar.edu.utn.frba.dds.medioscontacto.MedioContacto;
import ar.edu.utn.frba.dds.medioscontacto.Telefono;

import java.util.Objects;
import java.time.LocalDate;

public class Persona {
  private String nombreIdentificador; // nombre completo - razon social
  private Mail mail;
  private Telefono telefono;
  private Identificacion identificacion;
  private TipoPersona tipoPersona;
  MedioContacto medioPreferido;
  private LocalDate ultimaActividad;

  public Persona(String nombreIdentificador, Mail mail, Identificacion identificacion, TipoPersona tipoPersona) {
    this.nombreIdentificador = nombreIdentificador;
    this.mail = mail;
    this.identificacion = identificacion;
    this.tipoPersona = tipoPersona;
    this.medioPreferido = mail; //por defecto
    this.ultimaActividad = LocalDate.now();
  }

  public void registrarActividad() {
    this.ultimaActividad = LocalDate.now();
  }

  public LocalDate getUltimaActividad() {
    return ultimaActividad;
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
  }
}
