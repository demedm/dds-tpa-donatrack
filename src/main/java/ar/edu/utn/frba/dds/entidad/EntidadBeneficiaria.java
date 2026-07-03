package ar.edu.utn.frba.dds.entidad;

import ar.edu.utn.frba.dds.medioscontacto.Mail;
import ar.edu.utn.frba.dds.medioscontacto.Telefono;
import ar.edu.utn.frba.dds.necesidad.NecesidadHandler.NecesidadHandler;

import java.util.List;

public class EntidadBeneficiaria {
  private String razonSocial;
  private String direccion;
  private String tipo;
  private List<Mail> mailsContacto;
  private Telefono telefono;

  public EntidadBeneficiaria(String razon, String tipoEntidad,
                             Telefono telefonoOrganizacion,
                             List<Mail> contactoRepresentantes, String direccionEntidad) {
    contactoRepresentantes.forEach( mail -> {
      this.mailsContacto.add(mail);
    });
    this.razonSocial = razon;
    this.telefono = telefonoOrganizacion;
    this.direccion = direccionEntidad;
    this.tipo = tipoEntidad; //escuelas rurales, comedores, espacios de tutoría de niños, entre otros
  }

  public String getTipoEntidad() {
    return tipo;
  }

  public String getDireccion() {
    return direccion;
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
}