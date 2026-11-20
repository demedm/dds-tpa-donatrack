package ar.edu.utn.frba.dds.model;

public enum EstadoCamion {
  DISPONIBLE("DISPONIBLE"),
  EN_MANTENIMIENTO("EN MANTENIMIENTO"),
  RUTA_ASIGNADA("RUTA ASIGNADA"),
  REALIZANDO_ENTREGAS("REALIZANDO ENTREGAS");

  private final String nombre;
  EstadoCamion(String nombre) {
    this.nombre = nombre;
  }

  public String getNombre() {
    return this.nombre;
  }

  public static EstadoCamion strTo(String str) {
    for (EstadoCamion r : values()) {
      if (r.nombre.equals(str)) {
        return r;
      }
    }
    throw new RuntimeException("El nombre pasado no corresponde con un estado de ruta.");
  }
}
