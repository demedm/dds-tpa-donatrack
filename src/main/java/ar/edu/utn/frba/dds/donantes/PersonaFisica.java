package ar.edu.utn.frba.dds.donantes;

import ar.edu.utn.frba.dds.Bienes.Donacion;
import ar.edu.utn.frba.dds.medioscontacto.Mail;
import ar.edu.utn.frba.dds.medioscontacto.MedioContacto;

public class PersonaFisica extends Persona {
  private String apellido;
  private int edad;
  private Genero genero;
  private String direccionActual;

  public PersonaFisica(Mail mail, String nombreCompleto, Identificacion identificacion,
                       MedioContacto telefono, int edad, Genero genero,
                       String direccion) {
    super(nombreCompleto, mail, identificacion, TipoPersona.FISICA);
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
    return super.getMedioPreferido();
  }

  public void setContactoPreferencia(MedioContacto contactoPreferencia) {
    super.setMedioPreferido(contactoPreferencia);
  }

}
