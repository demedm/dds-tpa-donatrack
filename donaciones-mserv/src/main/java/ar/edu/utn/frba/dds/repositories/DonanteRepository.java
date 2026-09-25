package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.importerdonantes.ImporterDonantes;
import ar.edu.utn.frba.dds.model.notificaciones.Notificador;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Implementamos WithSimplePersistenceUnit para acceder a entityManager y withTransaction
public class DonanteRepository implements WithSimplePersistenceUnit {
  public static DonanteRepository Instance = new DonanteRepository();

  // Eliminamos la lista en memoria: private List<Persona> registroDonantes = new ArrayList<>();

  private final Notificador notificador = new Notificador();

  public void registrarDonante(Persona nuevoDonante) {

    // Usamos el campo direccion de la clase embebida Mail
    Optional<Persona> existente = buscarPorEmail(nuevoDonante.getMail().getMedioContacto());

    withTransaction(() -> {
      if (existente.isPresent()) {
        Persona personaGuardada = existente.get();
        personaGuardada.actualizarInfo(nuevoDonante);
        entityManager().merge(personaGuardada); // Actualizamos en la base de datos
        System.out.println("Donante actualizado");

      } else {
        entityManager().persist(nuevoDonante); // Insertamos en la base de datos
        System.out.println("Nuevo donante registrado");

        // Notificamos solo a los nuevos
        notificador.enviarNotificacionA(nuevoDonante, "Bienvenido a DonaTrack!");
      }
    });
  }

  /*public void importarDonantesDesdeCSVFile(String filePath) {
    new ImporterDonantes().importarDonantes(filePath, this);
  */

  public Optional<Persona> buscarPorEmail(String direccionMail) {
    // Consulta orientada a objetos (HQL). p.mail.direccion hace referencia a la propiedad dentro del embeddable
    return entityManager()
        .createQuery("SELECT p FROM Persona p WHERE p.mail.direccion = :email", Persona.class)
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
    // HQL para comparar fechas directo en la base de datos
    return entityManager()
        .createQuery("SELECT p FROM Persona p WHERE p.ultimaActividad < :fecha", Persona.class)
        .setParameter("fecha", fecha)
        .getResultList();
  }

  public List<Persona> getRegistroDonantes() {
    // Retorna todos los registros de la tabla persona (fisicas y juridicas)
    return entityManager()
        .createQuery("SELECT p FROM Persona p", Persona.class)
        .getResultList();
  }
}