package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.importerdonantes.ImporterDonantes;
import ar.edu.utn.frba.dds.model.notificaciones.Notificador;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DonanteRepository implements WithSimplePersistenceUnit {
  public static DonanteRepository Instance = new DonanteRepository();

  private final Notificador notificador = new Notificador();

  public void registrarDonante(Persona nuevoDonante) {

    Optional<Persona> existente = buscarPorEmail(nuevoDonante.getMail().getMedioContacto());

    withTransaction(() -> {
      if (existente.isPresent()) {
        Persona personaGuardada = existente.get();
        personaGuardada.actualizarInfo(nuevoDonante);
        entityManager().merge(personaGuardada);
        System.out.println("Donante actualizado");

      } else {
        entityManager().persist(nuevoDonante);
        System.out.println("Nuevo donante registrado");

      //  notificador.enviarNotificacionA(nuevoDonante, "Bienvenido a DonaTrack!");
      }
    });
  }

  public Optional<Persona> buscarPorEmail(String direccionMail) {
    return entityManager()
        .createQuery("SELECT p FROM Persona p WHERE p.mail.direccionMail = :email", Persona.class)
        .setParameter("email", direccionMail)
        .getResultList()
        .stream()
        .findFirst();
  }

  public boolean eliminarPorEmail(String direccionMail) {
    Optional<Persona> existente = buscarPorEmail(direccionMail);
    if (existente.isPresent()) {
      withTransaction(() -> {
        entityManager().remove(existente.get());
      });
      return true;
    }
    return false;
  }

  public List<Persona> buscarInactivosDesde(LocalDate fecha) {
    // Solo dejamos la consulta HQL a la base de datos
    return entityManager()
        .createQuery("SELECT p FROM Persona p WHERE p.ultimaActividad < :fecha", Persona.class)
        .setParameter("fecha", fecha)
        .getResultList();
  }

  public List<Persona> getRegistroDonantes() {
    return entityManager()
        .createQuery("SELECT p FROM Persona p", Persona.class)
        .getResultList();
  }
}