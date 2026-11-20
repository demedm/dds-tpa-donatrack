package ar.edu.utn.frba.dds.repositorios;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UbicacionesRepositorio {
  public static final UbicacionesRepositorio Instance = new UbicacionesRepositorio();
  private final Map<String, Map<String, Object>> ultimas = new ConcurrentHashMap<>();

  public void guardar(String patente, double lat, double lng) {
    ultimas.put(patente, Map.of(
        "patente", patente,
        "latitud", lat,
        "longitud", lng,
        "timestamp", System.currentTimeMillis()));
  }

  public Collection<Map<String, Object>> todas() { return ultimas.values(); }
}
