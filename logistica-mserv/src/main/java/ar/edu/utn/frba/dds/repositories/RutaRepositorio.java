package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.EstadoRuta;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.model.accionesrutas.AccionesSobreRutas;
import ar.edu.utn.frba.dds.model.accionesrutas.AsignarCamion;
import ar.edu.utn.frba.dds.model.accionesrutas.LoggearRuta;
import ar.edu.utn.frba.dds.model.accionesrutas.NotificarSobreRuta;
import ar.edu.utn.frba.dds.model.accionesrutas.ReplanificarRuta;
import ar.edu.utn.frba.dds.scripts.dto.ResponsePlanificacionDto;
import ar.edu.utn.frba.dds.scripts.dto.RutaPlanificadaDto;
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
    observers.forEach(observer -> observer.actualizarRuta(ruta, true));
    ruta.getEntregas().forEach(EntregaRepositorio.Instance::notificarInicioDeEntrega);
  }

  public void registrar(Ruta ruta) {
    entityManager().persist(ruta);
  }

  public void eliminarRuta(Ruta ruta) {
    entityManager().remove(ruta);
  }

  @SuppressWarnings("unchecked")
  public List<Ruta> mostrarTodos() {
    return entityManager()
        .createQuery("from Ruta")
        .getResultList();
  }

  public List<Ruta> mostrarRutasActivasConChoferAsignado() {
    var rutas = buscarRutasConEstado(EstadoRuta.NO_INICIADA)
        .stream().filter(ruta -> ruta.getChofer() != null).toList();
    rutas.addAll(buscarRutasConEstado(EstadoRuta.EN_CURSO));
    return rutas;
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

   */

  // RutaPlanificadaDto --> Ruta
  private Ruta crearRuta(RutaPlanificadaDto rutaDto) {
    var entregas = rutaDto.getDestinos().stream().map(destinoDto ->
        new Entrega(destinoDto.getDireccion(), destinoDto.getDonacionId())).toList();
    // despues de crearse las entregas se persisten
    entregas.forEach(EntregaRepositorio.Instance::registrar);

    Ruta ruta = new Ruta(entregas);
    ruta.asignarCamion(rutaDto.getCamion());
    return ruta;
  }

  public void asignarChoferes(List<Ruta> rutas) {
    var choferes = UsuarioRepositorio.Instance.mostrarChoferesNoAsignados();
    for (int i = 0; i < choferes.size(); i++) {
      var ruta = rutas.get(i);
      if (ruta != null) {
        ruta.asignarChofer(choferes.get(i));
      }
    }
    // no contempla el caso en el que rutas.size() > choferes.size()
  }

  public void recibirRespuestaPlanificacion(ResponsePlanificacionDto respuesta) {
    List<RutaPlanificadaDto> rutasPlanificadas = respuesta.getRutasPlanificadas();
    List<Ruta> rutas = rutasPlanificadas.stream().map(this::crearRuta).toList();
    // persisto rutas creadas
    rutas.forEach(this::registrar);
    // asignar los choferes
    asignarChoferes(rutas);
    // replanificar (pendiente)
  }

}