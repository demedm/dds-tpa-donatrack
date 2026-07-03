package ar.edu.utn.frba.dds.entidad;

import ar.edu.utn.frba.dds.api.dtos.EntidadBeneficiariaDTO;
import ar.edu.utn.frba.dds.medioscontacto.Mail;
import ar.edu.utn.frba.dds.medioscontacto.Telefono;
import ar.edu.utn.frba.dds.necesidad.NecesidadHandler.NecesidadHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntidadBeneficiaria {
  private String razonSocial;
  private String direccion;
  private String tipo;
  private List<Mail> mailsContacto = new ArrayList<>();
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

  public String getRazonSocial() {
    return razonSocial;
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

  public void actualizarDatos(EntidadBeneficiariaDTO dto) {
    if (dto.telefono != null) {
      this.telefono = new Telefono(dto.telefono);
    }
    if (dto.mails != null) {
      this.mailsContacto = dto.mails.stream()
          .map(Mail::new)
          .collect(Collectors.toList());
    }

    if(dto.direccion != null) {
      this.direccion = dto.direccion;
    }
  }
}