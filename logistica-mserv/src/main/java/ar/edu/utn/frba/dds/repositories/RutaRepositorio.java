package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.EstadoRuta;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.model.accionesrutas.AccionesSobreRutas;
import ar.edu.utn.frba.dds.model.accionesrutas.AsignarCamion;
import ar.edu.utn.frba.dds.model.accionesrutas.LoggearRuta;
import ar.edu.utn.frba.dds.model.accionesrutas.NotificarSobreRuta;
import ar.edu.utn.frba.dds.model.accionesrutas.ReplanificarRuta;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.ArrayList;
import java.util.List;

public class RutaRepositorio implements WithSimplePersistenceUnit {
  public static final RutaRepositorio Instance = new RutaRepositorio();
  private PlanificacionRutas planificadorRutas;
  List<AccionesSobreRutas> observers = new ArrayList<>();

  public RutaRepositorio() {
    agregarObserver(new LoggearRuta());
    agregarObserver(new ReplanificarRuta());
    agregarObserver(new AsignarCamion());
    agregarObserver(new NotificarSobreRuta());
  }

  private void agregarObserver(AccionesSobreRutas observer) {
    observers.add(observer);
  }

  public void eliminarObserver(AccionesSobreRutas observer) {
    observers.remove(observer);
  }

  /*
  public void addRutasPlanificadas(List<Ruta> nuevasRutasPlanificadas) {
    nuevasRutasPlanificadas.forEach(this::addRuta);
  }

  public void addRuta(Ruta ruta) {
    allRutas.add(ruta);
    observers.forEach(observer -> observer.actualizarRuta(ruta, true));
  }
  */

  public void iniciarRuta(Long idRuta) {
    Ruta ruta = buscarPorId(idRuta);
    ruta.iniciarRuta(); // cambia estado de Ruta y de cada Entrega
    ruta.getEntregas().forEach(EntregaRepositorio.Instance::notificarInicioDeEntrega);
  }

  public void registrar(Ruta ruta) {
    entityManager().persist(ruta);
    observers.forEach(observer -> observer.actualizarRuta(ruta, true));
  }

  @SuppressWarnings("unchecked")
  public List<Ruta> mostrarTodos() {
    return entityManager()
        .createQuery("from Ruta")
        .getResultList();
  }

  @SuppressWarnings("unchecked")
  public Ruta buscarPorId(Long id) {
    return (Ruta) entityManager()
        .createQuery("from Ruta where id = :id")
        .setParameter("id", id)
        .getResultList().stream().findFirst().orElse(null);
  }

  @SuppressWarnings("unchecked")
  public List<Ruta> buscarRutasDeCamion(Long idCamion) {
    return entityManager()
        .createQuery("from Ruta r where r.camion.id = :id")
        .setParameter("id", idCamion)
        .getResultList();
  }

  public Ruta buscarRutaEnCursoDeCamion(Long idCamion) {
    return entityManager()
        .createQuery("from Ruta r where r.camion.id = :id and r.estado = :estado",
            Ruta.class)
        .setParameter("id", idCamion)
        .setParameter("estado", EstadoRuta.EN_CURSO)
        .getResultList().stream().findFirst().orElse(null);
  }

  @SuppressWarnings("unchecked")
  public Ruta buscarRutasDeCamionConEstado(Long idCamion, EstadoRuta estadoRuta) {
    return entityManager()
        .createQuery("from Ruta r where r.camion.id = :id and r.estado = :estado",
            Ruta.class)
        .setParameter("id", idCamion)
        .setParameter("estado", estadoRuta)
        .getResultList().stream().findFirst().orElse(null);
  }

  @SuppressWarnings("unchecked")
  public List<Ruta> buscarRutasDeChofer(Long idChofer) {
    return entityManager()
        .createQuery("from Ruta r where r.chofer.id = :id")
        .setParameter("id", idChofer)
        .getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<Ruta> buscarRutasConEstado(EstadoRuta estado) {
    return entityManager()
        .createQuery("from Ruta r where r.estado = :estado")
        .setParameter("estado", estado)
        .getResultList();
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