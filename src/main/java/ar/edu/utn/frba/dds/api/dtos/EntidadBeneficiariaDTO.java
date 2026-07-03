package ar.edu.utn.frba.dds.api.dtos;

import ar.edu.utn.frba.dds.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.medioscontacto.Mail;
import ar.edu.utn.frba.dds.medioscontacto.Telefono;

import java.util.List;
import java.util.stream.Collectors;

public class EntidadBeneficiariaDTO {
  public String razonSocial;
  public String direccion;
  public String tipo;
  public List<String> mails;
  public String telefono;

  public EntidadBeneficiaria convertirDtoAObjeto() {
    return new EntidadBeneficiaria(
        razonSocial,
        tipo,
        new Telefono(telefono),
        mails.stream().map(Mail::new).collect(Collectors.toList()),
        direccion
    );
  }
}
