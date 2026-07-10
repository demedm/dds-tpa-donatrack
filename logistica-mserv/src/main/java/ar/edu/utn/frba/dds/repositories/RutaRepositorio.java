package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.model.accionesrutas.AccionesSobreRutas;import ar.edu.utn.frba.dds.model.accionesrutas.AsignarCamion;import ar.edu.utn.frba.dds.model.accionesrutas.LoggearRuta;import ar.edu.utn.frba.dds.model.accionesrutas.NotificarSobreRuta;import ar.edu.utn.frba.dds.model.accionesrutas.ReplanificarRuta;

import java.util.ArrayList;
import java.util.List;

public class RutaRepositorio {
  public static RutaRepositorio Instance = new RutaRepositorio();
  private List<Ruta> allRutas = new ArrayList<>();
  private PlanificacionRutas planificadorRutas;
  List<AccionesSobreRutas> observers = new ArrayList<>();

  public RutaRepositorio() {
    agregarObserver(new LoggearRuta());
    agregarObserver(new ReplanificarRuta());
    agregarObserver(new AsignarCamion());
    agregarObserver(new NotificarSobreRuta());
  }

  public void agregarObserver(AccionesSobreRutas observer) {
    observers.add(observer);
  }

  public void eliminarObserver(AccionesSobreRutas observer) {
    observers.remove(observer);
  }

  private List<Entrega> donacionesSinAsignar = new ArrayList<>();

  public Ruta findByid(String id) {
    return allRutas.stream().filter(ruta -> ruta.getId().equals(id)).toList().get(0);
  }

  public List<Ruta> getAllRutas() {
    return this.allRutas;
  }

  public void setAllRutas(List<Ruta> nuevasRutas) {
    allRutas.addAll(nuevasRutas);
  }

  public Entrega findEntregaById(String idRuta, String idEntrega) {
    var rutaBuscada = allRutas.stream().filter(ruta -> ruta.getId().equals(idRuta))
        .findFirst().orElse(null);
    return rutaBuscada.getEntregas().stream().filter(entrega ->
        entrega.getId().equals(idEntrega)).findFirst().orElse(null);
  }

  public void addRutasPlanificadas(List<Ruta> nuevasRutasPlanificadas) {
    nuevasRutasPlanificadas.forEach(this::addRuta);
  }

  public void addRuta(Ruta ruta) {
    allRutas.add(ruta);
    observers.forEach(observer -> observer.actualizarRuta(ruta, true));
  }

  /*
  public void gestionarRutas(List<Entrega> entregas) {
    int tamanioLote = 100;

    for (int i = 0; i < entregas.size(); i += tamanioLote) {
      List<Entrega> lote = entregas.subList(i, Math.min(i + tamanioLote, entregas.size()));

      this.planificadorRutas.solicitudPlanificacion(
          lote, this.flota.getCamionesDisponibles()
      );
    }
  }

  public void recibirRespuesta(PlanificacionRutasResponse respuesta) {

    if (respuesta.getDonacionesSinAsignar() != null) {
      this.donacionesSinAsignar.addAll(respuesta.getDonacionesSinAsignar());

    }

    respuesta.getRutas().stream()
        .map(RutaAdapter::rutaExternaToRuta)
        .forEach(ruta -> {
          boolean asignada = flota.asignarRutaACamion(ruta);
          accionesSobreRutas.forEach(accion ->
              accion.actualizarRuta(ruta, asignada));
        });
  }
*/

}