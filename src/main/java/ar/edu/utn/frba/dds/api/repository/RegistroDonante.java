package ar.edu.utn.frba.dds.api.repository;

import ar.edu.utn.frba.dds.donantes.Persona;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RegistroDonante {
  private static List<Persona> registroDonantes = new ArrayList<>();
  //private final Notificador notificador = new Notificador();

  public RegistroDonante() {
  }

  public static Persona registrarDonante(Persona nuevoDonante) {
    Optional<Persona> existente = buscarPorEmail(nuevoDonante.getMail().toString());

    if (existente.isPresent()) {
      existente.get().actualizarDatosDonantes(nuevoDonante);
      System.out.println("Donante actualizado");
      return existente.get();
    } else {
      registroDonantes.add(nuevoDonante);
      //notificador.enviarNotificacionA(nuevoDonante, "Bienvenido a DonaTrack!");

          System.out.println("Nuevo donante registrado");

      System.out.println("Nuevo donante registrado");
      return nuevoDonante;
    }
  }

  public static Optional<Persona> buscarPorEmail(String direccionMail) {
    return registroDonantes.stream()
        .filter(d -> d.getMail().getMedioContacto().equals(direccionMail))
        .findFirst();
  }

  public static void actualizarDonante(String email, Persona nuevaInfo) {
    Persona existente = buscarPorEmail(email)
        .orElseThrow(() -> new RuntimeException("No se encontró donante con email: " + email));

    existente.actualizarDatosDonantes(nuevaInfo);
  }

  public List<Persona> buscarInactivosDesde(LocalDate fecha) {
    // estoy implementando esto: pendiente
    return null;
  }

  public List<Persona> getRegistroDonantes() {
    return registroDonantes;
  }

}
