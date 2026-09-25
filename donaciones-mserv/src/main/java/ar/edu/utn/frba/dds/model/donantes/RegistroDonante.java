package ar.edu.utn.frba.dds.model.donantes;
import ar.edu.utn.frba.dds.model.notificaciones.Notificador;
import ar.edu.utn.frba.dds.model.importerdonantes.ImporterDonantes;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RegistroDonante implements WithSimplePersistenceUnit {

  // ¡Se elimina la lista en memoria! Ahora todo va directo a la base de datos.

  // Para hacerla Singleton (como usás en tu controller DonanteRepository.Instance)
  public static final RegistroDonante Instance = new RegistroDonante();

  private final Notificador notificador = new Notificador();

  public RegistroDonante() {
  }

  public void registrarDonante(Persona nuevoDonante) {
    // Usamos el campo direccion de la clase embebida Mail
    Optional<Persona> existente = buscarPorEmail(nuevoDonante.getMail().getMedioContacto());

    // jpa-extras nos provee withTransaction para manejar el begin/commit automáticamente
    withTransaction(() -> {
      if (existente.isPresent()) {
        Persona personaGuardada = existente.get();
        personaGuardada.actualizarInfo(nuevoDonante);
        entityManager().merge(personaGuardada); // Actualizamos en BD
        System.out.println("Donante actualizado");
      } else {
        entityManager().persist(nuevoDonante); // Insertamos en BD
        System.out.println("Nuevo donante registrado");
        // Mandamos la notificación
        notificador.enviarNotificacionA(nuevoDonante, "Bienvenido a DonaTrack!");
      }
    });
  }

  public void importarDonantesDesdeCSVFile(String filePath) {
    new ImporterDonantes().importarDonantes(filePath, this);
  }

  public Optional<Persona> buscarPorEmail(String direccionMail) {
    // Consulta HQL buscando por la columna embebida
    return entityManager()
        .createQuery("SELECT p FROM Persona p WHERE p.mail.direccion = :email", Persona.class)
        .setParameter("email", direccionMail)
        .getResultList()
        .stream()
        .findFirst();
  }

  public List<Persona> buscarInactivosDesde(LocalDate fecha) {
    // Ya te dejo armada la consulta de inactivos para cuando envíes las notificaciones por inactividad
    return entityManager()
        .createQuery("SELECT p FROM Persona p WHERE p.ultimaActividad <= :fecha", Persona.class)
        .setParameter("fecha", fecha)
        .getResultList();
  }

  public List<Persona> getRegistroDonantes() {
    // Devuelve todos los registros de la tabla persona
    return entityManager()
        .createQuery("SELECT p FROM Persona p", Persona.class)
        .getResultList();
  }

  // Agrego este método que es el que estabas usando en DonanteController.java
  public boolean eliminarPorEmail(String email) {
    Optional<Persona> existente = buscarPorEmail(email);
    if (existente.isPresent()) {
      withTransaction(() -> {
        entityManager().remove(existente.get()); // Borra de la BD
      });
      return true;
    }
    return false;
  }
}