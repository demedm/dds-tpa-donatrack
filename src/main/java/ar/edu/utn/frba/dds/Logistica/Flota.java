package ar.edu.utn.frba.dds.Logistica;

import java.util.ArrayList;
import java.util.List;

public class Flota {
  private List<Camion> allCamiones = new ArrayList<>();

  public Flota(List<Camion>camiones) {
    this.allCamiones.addAll(camiones);
  }

  // devuelve false si no se pudo asignar
  public boolean asignarRutaACamion(Ruta ruta) {
    var patenteCamion = ruta.getPatenteAsignada();
    var camionAsignado = allCamiones.stream()
        .filter(camion -> camion.getPatente() == patenteCamion)
        .findFirst().orElse(null);

    if(camionAsignado != null && camionAsignado.asignarRuta(ruta)) {
      // notificar entidades que se inicio la ruta: pendiente
      return true;
    }
    return false;
  }

  public List<Ruta> asignarRutasACamiones(List<RutaComponenteExterno> rutasAsignadas) {
    List<Ruta> rutas = rutasAsignadas.stream().map(ruta ->
        new RutaAdapter().rutaExternaToRuta(ruta)).toList();
    List<Ruta> rutasNoAsignadas = new ArrayList<>();

    rutas.forEach(ruta -> {
      Camion camion = allCamiones.stream()
          .filter(c -> c.getPatente().equals(ruta.getPatenteAsignada()))
          .findFirst()
          .orElse(null);

      if(camion == null || !camion.asignarRuta(ruta)) {
        rutasNoAsignadas.add(ruta);
      }
    });
    return rutasNoAsignadas;
  }

  public List<Camion> getCamionesDisponibles() {
    List<Camion> camionesDisponibles = this.allCamiones.stream().filter(camion ->
        camion.getEstado().equals(EstadoCamion.DISPONIBLE)).toList();
    return camionesDisponibles;
  }

}
