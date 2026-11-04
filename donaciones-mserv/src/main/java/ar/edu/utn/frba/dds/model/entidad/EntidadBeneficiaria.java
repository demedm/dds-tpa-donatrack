package ar.edu.utn.frba.dds.model.entidad;

import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.medioscontacto.Telefono;
import ar.edu.utn.frba.dds.model.necesidad.Necesidad;
import java.util.ArrayList;
import java.util.List;
import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;
import ar.edu.utn.frba.dds.model.notificaciones.Destinatario;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "entidades_beneficiarias")

public class EntidadBeneficiaria implements Destinatario{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String razonSocial;
  private String direccion;
  private String tipo;

  @ElementCollection
  @CollectionTable(name = "entidad_mails", joinColumns =
  @JoinColumn(name = "entidad_id"))
  private List<Mail> mailsContacto = new ArrayList<>();

  @Embedded
  private Telefono telefono;

  @OneToMany
  @JoinColumn(name = "entidadAsignadaId", insertable = false, updatable = false)
  private List<DonacionSegmentada> donacionesRecibidas = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "entidadId", insertable = false, updatable = false)
  private List<Necesidad> necesidades = new ArrayList<>();

  public EntidadBeneficiaria() {}

  public EntidadBeneficiaria(String direccion, List<Mail> mails, String razonSocial, Telefono telefono, String tipo) {
    this.direccion = direccion;
    this.mailsContacto = mails;
    this.razonSocial = razonSocial;
    this.telefono = telefono;
    this.tipo = tipo;

  }

  public Long getId() {
    return this.id;
  }

  public String getRazonSocial() {
    return this.razonSocial;
  }

  public void setId(Long id) {
    this.id = id;
  }
  public void setRazonSocial(String razonSocial) {
    this.razonSocial = razonSocial;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public void setTelefono(Telefono telefono) {
    this.telefono = telefono;
  }

  public Telefono getTelefono() {
    return telefono;
  }
  public void setMailsContacto(List<Mail> mailsContacto) {
    this.mailsContacto = mailsContacto;
  }

  public List<DonacionSegmentada> getDonacionesRecibidas() {
    return this.donacionesRecibidas;
  }

  public String getTipoEntidad() {
    return tipo;
  }

  public String getDireccion() {
    return direccion;
  }

  public List<Mail> getMailsContacto() {
    return this.mailsContacto;
  }

  @Override
  public MedioContacto medioDeContacto() {
    if (mailsContacto != null && !mailsContacto.isEmpty()) {
      return mailsContacto.get(0);
    }
    return telefono;
  }
/*
  public Necesidad crearNecesidad(GestorNecesidades gestor) {
    Necesidad necesidad = new Necesidad(this);
    gestor.agregarNecesidad(necesidad);
    return necesidad;
  }
  public Necesidad crearNecesidad(GestorNecesidades gestor){
    Necesidad necesidad = new Necesidad(this);
    gestor.agregarNecesidad(necesidad);
    return necesidad;
  }
*/

  public List<Necesidad> getNecesidades() {
    return necesidades;
  }

  @Override
  public String nombreParaMostrar() {
    return razonSocial;
  }

  // Toda necesidad nueva queda apuntando a esta entidad
  public void agregarNecesidad(Necesidad necesidad) {
    necesidad.setEntidadId(this.id);
    this.necesidades.add(necesidad);
  }


}