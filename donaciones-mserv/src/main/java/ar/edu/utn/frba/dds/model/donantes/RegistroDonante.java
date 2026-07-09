package ar.edu.utn.frba.dds.model.donantes;

import ar.edu.utn.frba.dds.model.notificaciones.Notificador;
import ar.edu.utn.frba.dds.model.importerdonantes.ImporterDonantes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RegistroDonante {
  private List<Persona> registroDonantes = new ArrayList<>();
  private final Notificador notificador = new Notificador();

  public RegistroDonante() {
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

  public void importarDonantesDesdeCSVFile(String filePath) {
    new ImporterDonantes().importarDonantes(filePath, this);
  }

  public Optional<Persona> buscarPorEmail(String direccionMail) {
    return registroDonantes.stream()
        .filter(d -> d.getMail().getMedioContacto().equals(direccionMail))
        .findFirst();
  }

  public List<Persona> buscarInactivosDesde(LocalDate fecha) {
    // estoy implementando esto: pendiente
    return null;
  }

  public List<Persona> getRegistroDonantes() {
    return registroDonantes;
  }
}
