package ar.edu.utn.frba.dds.model.donantes;

import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;
import ar.edu.utn.frba.dds.model.medioscontacto.Telefono;

import java.time.LocalDate;
import ar.edu.utn.frba.dds.model.notificaciones.Destinatario;
import javax.persistence.*;


@Entity
@Table(name = "persona", schema = "donaciones")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_persona")
public abstract class Persona implements Destinatario{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nombre_identificador")
  private String nombreIdentificador;

  @Embedded
  private Identificacion identificacion;

  @Embedded
  private Mail mail;

  @Embedded
  private Telefono telefono;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_persona_enum")
  private TipoPersona tipoPersona;

  @Column(name = "ultima_actividad")
  private LocalDate ultimaActividad;

  @Transient // JPA no puede persistir interfaces nativamente. Conviene guardar un Enum (ej: MAIL, TELEFONO)
  private MedioContacto medioPreferido;

  public LocalDate getUltimaActividad() {
    return ultimaActividad;
  }

  public void setUltimaActividad(LocalDate ultimaActividad) {
    this.ultimaActividad = ultimaActividad;
  }


<<<<<<< HEAD

  public Persona() {}

  public Long getId() {
    return id;
=======
  public Persona() {
    this.ultimaActividad = LocalDate.now();
>>>>>>> 13de755f28e7f883e85daf2e2275cafd0e6c49ea
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
    this.ultimaActividad = LocalDate.now();
  }

  public void actualizarDatosComunes(String nombreIdentificador, String nroDocumento, String email, String telefono ) {

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
    this.ultimaActividad = LocalDate.now();
  }

  public Telefono getTelefono() {
    return telefono;
  }

  @Override
  public MedioContacto medioDeContacto() {
    if (medioPreferido != null) {
      return medioPreferido;
    }
    return getMail() != null ? getMail() : getTelefono();
  }

  @Override
  public String nombreParaMostrar() {
    return getNombreIdentificador();
  }
}