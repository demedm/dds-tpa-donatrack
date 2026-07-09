package ar.edu.utn.frba.dds.logistica.repositories;

import ar.edu.utn.frba.dds.logistica.modelo.Camion;
import ar.edu.utn.frba.dds.logistica.modelo.AccionesSobreRutas;
import ar.edu.utn.frba.dds.logistica.modelo.Entrega;
import ar.edu.utn.frba.dds.logistica.modelo.Ruta;

import java.util.ArrayList;
import java.util.List;

public class RutaRepositorio {
  public static RutaRepositorio Instance = new RutaRepositorio();
  private List<Ruta> allRutas = new ArrayList<>();
  private PlanificacionRutas planificadorRutas;
  private List<Ruta> rutasPendientesAsignar = new ArrayList<>();
  private List<AccionesSobreRutas> accionesSobreRutas = new ArrayList<>();

  private List<Entrega> donacionesSinAsignar = new ArrayList<>();

  public Ruta findByid(int id) {
    return allRutas.stream().filter(ruta -> ruta.getId() == id).toList().get(0);
  }

  public List<Ruta> getAllRutas() {
    return this.allRutas;
  }

  public void setAllRutas(List<Ruta> nuevasRutas) {
    allRutas.addAll(nuevasRutas);
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
  public List<Entrega> getDonacionesSinAsignar() {
    return donacionesSinAsignar;
  }
}
