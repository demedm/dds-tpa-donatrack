package ar.edu.utn.frba.dds.Logistica;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Flota {
  List<Camion> allCamiones = new ArrayList<>();
  List<Camion> camionesDisponibles;

  public Flota(List<Camion>camiones) {
    this.allCamiones = camiones;
  }

  public void actualizarCamionesDisponibles() {
    this.allCamiones.stream().filter(camion -> {
      boolean b = camion.getEstado() == EstadoCamion.DISPONIBLE;
      return b;
    });
  }

}
