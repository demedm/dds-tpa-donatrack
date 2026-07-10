package ar.edu.utn.frba.dds.repositories;


import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.EstadoCamion;

import java.util.ArrayList;
import java.util.List;

public class CamionRepositorio {
  private List<Camion> flota = new ArrayList<>();
  public static CamionRepositorio Instance = new CamionRepositorio();

  public Camion getRandom() {
    return flota.stream().findAny().orElse(null);
  }

  public Camion getByPatente(String patente) {
    var c = flota.stream().filter(camion ->
        camion.getPatente().equals(patente)).toList().get(0);
    return c;
  }

  public List<Camion> getFlota() {
    return this.flota;
  }

  public void registrarCamion(Camion camion) {
    flota.add(camion);
  }

  /*
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
  public Camion buscarCamionPorPatente(String patente) {
    return this.allCamiones.stream()
        .filter(camion -> camion.getPatente().equals(patente))
        .findFirst()
        .orElse(null);
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
  // Asumiendo que tu lista se llama "camiones". Si se llama distinto
  // (ej: "listaCamiones"), cambialo en el return.
  public List<Camion> getCamiones() {
    return this.allCamiones;
  }
  */

  public List<Camion> getCamionesDisponibles() {
    List<Camion> camionesDisponibles = this.flota.stream().filter(camion ->
        camion.getEstado().equals(EstadoCamion.DISPONIBLE)).toList();
    return camionesDisponibles;
  }

}