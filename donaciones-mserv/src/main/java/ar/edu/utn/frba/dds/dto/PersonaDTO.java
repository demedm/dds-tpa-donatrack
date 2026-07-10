package ar.edu.utn.frba.dds.dto;

import ar.edu.utn.frba.dds.model.donantes.Identificacion;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.donantes.TipoDocumento;
import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.medioscontacto.Telefono;

import java.time.LocalDate;

public abstract class PersonaDTO {
  protected String nombreIdentificador;
  protected String email;
  protected String telefono;
  protected String tipoDocumento;
  protected String nroDocumento;

  public String getNombreIdentificador() {
    return nombreIdentificador;
  }

  public void setNombreIdentificador(String nombreIdentificador) {
    this.nombreIdentificador = nombreIdentificador;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getTipoDocumento() {
    return tipoDocumento;
  }

  public void setTipoDocumento(String tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  public String getNroDocumento() {
    return nroDocumento;
  }

  public void setNroDocumento(String nroDocumento) {
    this.nroDocumento = nroDocumento;
  }

  public abstract Persona PersonaDto();

  public abstract void aplicarCambios(Persona persona);

  protected void completarDatosComunesParaCreacion(Persona persona) {
    persona.setNombreIdentificador(nombreIdentificador);
    persona.setIdentificacion(new Identificacion(TipoDocumento.valueOf(tipoDocumento), nroDocumento));
    persona.setMail(new Mail(email));
    persona.setTelefono(new Telefono(telefono));
    persona.setUltimaActividad(LocalDate.now());
  }

  protected void aplicarCambiosActualizacionComunes(Persona persona) {
    persona.actualizarDatosComunes(nombreIdentificador, nroDocumento, email, telefono);
  }
}
