package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.importerdonantes.ImporterDonantes;
import ar.edu.utn.frba.dds.model.notificaciones.Notificador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DonanteRepository {
  public static DonanteRepository Instance = new DonanteRepository();

  private List<Persona> registroDonantes = new ArrayList<>();
  private final Notificador notificador = new Notificador();

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

  /*public void importarDonantesDesdeCSVFile(String filePath) {
    new ImporterDonantes().importarDonantes(filePath, this);
  */

  public Optional<Persona> buscarPorEmail(String direccionMail) {
    return registroDonantes.stream()
        .filter(d -> d.getMail().getMedioContacto().equals(direccionMail))
        .findFirst();
  }

  public boolean eliminarPorEmail(String direccionMail) {
    Optional<Persona> existente = buscarPorEmail(direccionMail);
    existente.ifPresent(registroDonantes::remove);
    return existente.isPresent();
  }

  public List<Persona> buscarInactivosDesde(LocalDate fecha) {
    // estoy implementando esto: pendiente
    return null;
  }

  public List<Persona> getRegistroDonantes() {
    return registroDonantes;
  }
}
  
  /*
  private final Map<Long, Persona> almacenamiento = new ConcurrentHashMap<>();
  private final AtomicLong secuencia = new AtomicLong(0);

  public Persona save(Persona donante) {
    if (donante.getId() == null) {
      donante.setId(secuencia.incrementAndGet());
    }
    store.put(donante.getId(), donante);
    return donante;
  }

  public Optional<Persona> findById(Long id) {
    return Optional.ofNullable(store.get(id));
  }

  public List<Persona> findAll() {
    return new ArrayList<>(store.values());
  }

  public boolean existsById(Long id) {
    return store.containsKey(id);
  }

  public boolean deleteById(Long id) {
    return store.remove(id) != null;
  }

  public Optional<Persona> findByEmail(String email) {
    if (email == null) return Optional.empty();
    return store.values().stream()
        .filter(d -> email.equalsIgnoreCase(d.getEmail()))
        .findFirst();
  }

 */