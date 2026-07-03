package ar.edu.utn.frba.dds.api.dtos;

import ar.edu.utn.frba.dds.Bienes.Bien;
import ar.edu.utn.frba.dds.Bienes.Donacion;
import ar.edu.utn.frba.dds.api.repository.RegistroDonante;
import ar.edu.utn.frba.dds.donantes.Persona;

import java.util.List;
import java.util.stream.Collectors;

public class DonacionDTO {

  public String descripcionGeneral;
  public String emailDonante;
  public List<String> bienes;public List<BienDuraderoDTO> biensDuraderos;
  public List<BienPerecederoDTO> biensPerecederos;

  public Donacion convertirDtoAObjeto() {

    Persona donante = RegistroDonante.buscarPorEmail(emailDonante)
        .orElseThrow(() -> new RuntimeException("No se encontró donante con email: " + emailDonante));

    List<Bien> bienes = biensDuraderos.stream().map(BienDuraderoDTO::convertirDtoAObjeto).collect(Collectors.toList());

    bienes.addAll(biensPerecederos.stream().map(BienPerecederoDTO::convertirDtoAObjeto).toList());

    return new Donacion(descripcionGeneral, bienes, donante);
  }

}
