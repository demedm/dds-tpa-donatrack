package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;

import java.util.List;

public class EntidadRepository {
  public static EntidadRepository Instance = new EntidadRepository();
  private List<EntidadBeneficiaria> registro;

  public EntidadBeneficiaria obtenerPorId(String mailId) {
    List<EntidadBeneficiaria> registroEntidades = registro;
    EntidadBeneficiaria buscada = registroEntidades.stream()
        .filter(ent -> ent.getMailsContacto().stream()
            .anyMatch(mail -> mail.getMedioContacto().equals(mailId)))
        .findFirst()
        .orElse(null);

    return buscada;
  }

  public List<EntidadBeneficiaria> getRegistro() {
    return this.registro;
  }
}
