package ar.edu.utn.frba.dds.entidad;

import ar.edu.utn.frba.dds.medioscontacto.Mail;

import java.util.ArrayList;
import java.util.List;

public class EntidadRepository implements EntidadFunciones {
  private RegistroEntidades registro;

  @Override
  public EntidadBeneficiaria obtenerPorId(String mailId) {
    List<EntidadBeneficiaria> registroEntidades = this.registro.getRegistroEntidades();
    EntidadBeneficiaria buscada = registroEntidades.stream()
        .filter(ent -> ent.getMailsContacto().stream()
            .anyMatch(mail -> mail.getMedioContacto().equals(mailId)))
        .findFirst()
        .orElse(null);

    return buscada;
  }

  public RegistroEntidades getRegistro() {
    return this.registro;
  }
}
