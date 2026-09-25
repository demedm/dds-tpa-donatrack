package ar.edu.utn.frba.dds.model.donantes;
import javax.persistence.*;

import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;

@Entity
@DiscriminatorValue("FISICA")
public class PersonaFisica extends Persona {

  @Column(name = "apellido")
  private String apellido;

  @Column(name = "edad")
  private int edad;

  @Enumerated(EnumType.STRING)
  @Column(name = "genero")
  private Genero genero;

  @Column(name = "direccion_actual")
  private String direccionActual;

  public void setGenero(Genero genero) {
    this.genero = genero;
  }

  public void setEdad(int edad) {
    this.edad = edad;
  }

  public void setDireccionActual(String direccion) {
    this.direccionActual = direccion;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public Genero getGenero() {
    return this.genero;
  }

  public int getEdad() {
    return this.edad;
  }

  public String getApellido() {
    return this.apellido;
  }

  public String getDireccionActual() {
    return direccionActual;
  }

  @Override
  public void actualizarInfo(Persona nuevosDatos) {
    super.actualizarInfo(nuevosDatos);
    PersonaFisica nuevaFisica = (PersonaFisica) nuevosDatos;
    this.apellido = nuevaFisica.getApellido();
    this.edad = nuevaFisica.getEdad();
    this.genero = nuevaFisica.getGenero();
    this.direccionActual = nuevaFisica.getDireccionActual();
  }

}
