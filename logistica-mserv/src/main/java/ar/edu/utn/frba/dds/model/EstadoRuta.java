package ar.edu.utn.frba.dds.model;

public enum EstadoRuta {
  NO_INICIADA("NO_INICIADA"),
  EN_CURSO("EN_CURSO"),
  CANCELADA("CANCELADA"),
  FINALIZADA("FINALIZADA");

  private final String nombre;
  EstadoRuta(String nombre) {
    this.nombre = nombre;
  }

  public static EstadoRuta strTo(String str) {
    for (EstadoRuta r : values()) {
      if (r.nombre.equals(str)) {
        return r;
      }
    }
    throw new RuntimeException("El nombre pasado no corresponde con un estado de ruta.");
  }
}
