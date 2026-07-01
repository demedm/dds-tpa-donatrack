package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.donantes.Persona;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RegistroDonante {
  private List<Persona> registroDonantes = new ArrayList<>();
  private final Notificador notificador;

  public RegistroDonante(Notificador notificador) {
    this.notificador = notificador;
  }

  public void registrarDonante(Persona nuevoDonante) {

    Optional<Persona> existente = buscarPorEmail(nuevoDonante.getMail().toString());

    if (existente.isPresent()) {
      existente.get().actualizarInfo(nuevoDonante);
      System.out.println("Donante actualizado");

    } else {
      registroDonantes.add(nuevoDonante);
      notificador.enviarNotificacionA(nuevoDonante, "Bienvenido a DonaTrack!");

          System.out.println("Nuevo donante registrado");

    }
  }

  public Optional<Persona> buscarPorEmail(String direccionMail) {
    return registroDonantes.stream()
        .filter(d -> d.getMail().getMedioContacto().equals(direccionMail))
        .findFirst();
  }

  public List<Persona> getRegistroDonantes() {
    return registroDonantes;
  }
}
