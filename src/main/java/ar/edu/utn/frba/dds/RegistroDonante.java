package ar.edu.utn.frba.dds;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegistroDonante {
  public List<Donante> registroDonantes = new ArrayList<>();

  public void registrarDonante(Donante nuevoDonante) {

    Donante existente = buscarPorEmail(nuevoDonante.getMailContacto().getMedioContacto());

    if (existente != null) {
      existente.actualizarInfo(nuevoDonante.getNombre(), nuevoDonante.getDocumento().toString(), nuevoDonante.getTelefonoContacto());
      System.out.println("Donante actualizado");
    } else {
      registroDonantes.add(nuevoDonante);
      System.out.println("Nuevo donante registrado");
    }
  }

  private Donante buscarPorEmail(String direccionMail) {
    for (Donante donante : registroDonantes) {
      if (Objects.equals(donante.getMailContacto().getMedioContacto(), direccionMail)) {
        return donante;
      }
    }
    return null;
  }

}
