package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntidadRepository {
  public static EntidadRepository Instance = new EntidadRepository();
  private List<EntidadBeneficiaria> registro = new ArrayList<>();

  public void registrar(EntidadBeneficiaria entidad) {
    entidad.setId(UUID.randomUUID().toString());
    registro.add(entidad);
  }

  public EntidadBeneficiaria obtenerPorId(String id) {
    return registro.stream()
        .filter(ent -> ent.getId().equals(id))
        .findFirst()
        .orElse(null);
  }

  public List<EntidadBeneficiaria> obtenerEntidades() {
    return this.registro;
  }

  public EntidadBeneficiaria actualizarEntidad(EntidadBeneficiaria entidadExistente, EntidadBeneficiaria camposActualizar) {
    entidadExistente.setDireccion(camposActualizar.getDireccion());
    entidadExistente.setTelefono(camposActualizar.getTelefono());
    entidadExistente.setMailsContacto(camposActualizar.getMailsContacto());

    return entidadExistente;
  }

  public boolean eliminarPorId(String id) {
    EntidadBeneficiaria existente = obtenerPorId(id);
    if (existente == null) {
      return false;
    }
    registro.remove(existente);
    return true;
  }
}
