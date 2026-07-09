package ar.edu.utn.frba.dds.donaciones.api.repository;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
/*
@Repository
public class DonanteRepository {

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
}

 */