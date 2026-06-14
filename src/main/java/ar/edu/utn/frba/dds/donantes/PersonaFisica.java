package ar.edu.utn.frba.dds.donantes;

import ar.edu.utn.frba.dds.Bienes.Bien;
import ar.edu.utn.frba.dds.Bienes.Donacion;
import ar.edu.utn.frba.dds.medioscontacto.Mail;
import ar.edu.utn.frba.dds.medioscontacto.MedioContacto;
import ar.edu.utn.frba.dds.medioscontacto.Telefono;
import ar.edu.utn.frba.dds.necesidad.GestorDonaciones;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class PersonaFisica extends Persona {
  private String apellido;
  private int edad;
  private Genero genero;
  private String direccionActual;
  private MedioContacto contactoPreferencia;

  public PersonaFisica(Mail mail, String nombreCompleto, Identificacion identificacion,
                       MedioContacto telefono, int edad, Genero genero,
                       String direccion) {
    super(nombreCompleto, mail, identificacion);
    var nombre = nombreCompleto.split(" ");
    this.apellido = nombre[1];
    this.edad = edad;
    this.genero = genero;
    this.direccionActual = direccion;
  }

  public String getDireccionActual() {
    return direccionActual;
  }

  public MedioContacto getContactoPreferencia() {
    return contactoPreferencia;
  }

  public void setContactoPreferencia(MedioContacto contactoPreferencia) {
    this.contactoPreferencia = contactoPreferencia;
  }

}
