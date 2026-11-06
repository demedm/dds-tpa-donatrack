package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.notificaciones.EventoNotificacionService;
import ar.edu.utn.frba.dds.model.notificaciones.Notificador;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DonanteRepository implements WithSimplePersistenceUnit {

  public static DonanteRepository Instance = new DonanteRepository();

  private EventoNotificacionService notificaciones = EventoNotificacionService.Instance;

  public void setNotificaciones(EventoNotificacionService notificaciones) {
    this.notificaciones = notificaciones;
  }

  public boolean registrarDonante(Persona nuevoDonante) {

    Optional<Persona> existente = buscarPorEmail(nuevoDonante.getMail().getMedioContacto());

    if (existente.isPresent()) {
      Persona personaGuardada = existente.get();
      personaGuardada.actualizarInfo(nuevoDonante);
      return false;
    }

    entityManager().persist(nuevoDonante);
    return true;

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
      entityManager().remove(existente.get());
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

  public Persona buscarPorId(Long id) {
    return entityManager().find(Persona.class, id);
  }

}