package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.exceptions.NoHayChoferesDisponiblesException;
import ar.edu.utn.frba.dds.exceptions.RutaNotFoundException;
import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.EstadoRuta;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.model.accionesrutas.AccionesSobreRutas;
import ar.edu.utn.frba.dds.model.accionesrutas.AsignarCamion;
import ar.edu.utn.frba.dds.model.accionesrutas.LoggearRuta;
import ar.edu.utn.frba.dds.model.accionesrutas.NotificarSobreRuta;
import ar.edu.utn.frba.dds.model.accionesrutas.ReplanificarRuta;
import ar.edu.utn.frba.dds.model.usuarios.Chofer;
import ar.edu.utn.frba.dds.scripts.dto.ResponsePlanificacionDto;
import ar.edu.utn.frba.dds.scripts.dto.RutaDto;
import ar.edu.utn.frba.dds.scripts.dto.RutaPlanificadaDto;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

  private boolean iniciarTransaccion() {
    if (!entityManager().getTransaction().isActive()) {
      entityManager().getTransaction().begin();
      return true;
    }
    return false;
  }

  private void commit(boolean transaccionPropia) {
    if (transaccionPropia) {
      entityManager().getTransaction().commit();
    }
  }

  public Ruta iniciarRuta(Chofer chofer, Long idRuta) {
    EntityTransaction transaction = entityManager().getTransaction();
    boolean transaccionPropia = iniciarTransaccion();
    Ruta ruta = buscarPorId(idRuta);
    if (!Objects.equals(chofer.getId(), ruta.getChofer().getId())) {
      return null;
    }
    ruta.iniciarRuta(); // cambia estado de Ruta y de cada Entrega
    observers.forEach(observer -> observer.actualizarRuta(ruta, true));
    ruta.getEntregas().forEach(EntregaRepositorio.Instance::notificarInicioDeEntrega);
    commit(transaccionPropia);
    return ruta;
  }

  public void registrar(Ruta ruta) {
    EntityTransaction transaction = entityManager().getTransaction();
    boolean transaccionPropia = iniciarTransaccion();

    entityManager().persist(ruta);

    commit(transaccionPropia);
  }

  public void eliminarRuta(Ruta ruta) {
    EntityTransaction transaction = entityManager().getTransaction();
    boolean transaccionPropia = iniciarTransaccion();

    entityManager().remove(ruta);

    commit(transaccionPropia);
  }

  private void eliminar(Ruta ruta) {
    entityManager().remove(ruta);
  }

  @SuppressWarnings("unchecked")
  public List<Ruta> mostrarTodos() {
    return entityManager()
        .createQuery("from Ruta")
        .getResultList();
  }

  // devulve una lista de rutas o una lista vacia
  public List<Ruta> mostrarRutasActivasConChoferAsignado() {
    var rutasNoIniciadas = buscarRutasConEstado(EstadoRuta.NO_INICIADA);
    var rutasEnCurso = buscarRutasConEstado(EstadoRuta.EN_CURSO);
    List<Ruta> rutas = new ArrayList<>();

    if (!rutasNoIniciadas.isEmpty()) {
      rutas = rutasNoIniciadas.stream().filter(ruta -> ruta.getChofer() != null).toList();
    } else if (!rutasEnCurso.isEmpty()) {
      rutas.addAll(rutasEnCurso);
    }
    return rutas;
  }

  @SuppressWarnings("unchecked")
  public Ruta buscarPorId(Long id) {
    var ruta = entityManager()
        .createQuery("from Ruta where id = :id", Ruta.class)
        .setParameter("id", id)
        .getResultList().stream().findFirst().orElse(null);
    if (ruta == null) {
      throw new RutaNotFoundException(id);
    }
    return ruta;
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

  public EstadoRuta buscarEstadoPorId(Long id) {
    return entityManager()
        .createQuery("select r.estado from Ruta r where r.id = :id", EstadoRuta.class)
        .setParameter("id", id)
        .getSingleResult();
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
    Camion camion = CamionRepositorio.Instance.buscarPorPatente(rutaDto.getCamion().getPatente());
    var choferes = UsuarioRepositorio.Instance.mostrarChoferesNoAsignados();
    if (choferes.isEmpty()) {
      throw new NoHayChoferesDisponiblesException();
    }
    Ruta ruta = new Ruta(entregas);
    ruta.asignarCamion(camion);
    ruta.asignarChofer(choferes.get(0));
    return ruta;
  }

  public void recibirRespuestaPlanificacion(ResponsePlanificacionDto respuesta) {
    List<RutaPlanificadaDto> rutasPlanificadas = respuesta.getRutasPlanificadas();
    List<Ruta> rutas = rutasPlanificadas.stream().map(this::crearRuta).toList();
    // persisto rutas creadas
    rutas.forEach(this::registrar);
  }

  public Ruta actualizarRuta(Ruta ruta, RutaDto dto) {
    EntityTransaction transaction = entityManager().getTransaction();
    boolean transaccionPropia = iniciarTransaccion();
    if (dto.getChoferId() != null) {
      var chofer = UsuarioRepositorio.Instance.buscarChoferPorId(dto.getChoferId());
      ruta.asignarChofer(chofer);
    } else if (dto.getCamionId() != null) {
      var camion = CamionRepositorio.Instance.buscarPorId(dto.getCamionId());
      var camionActual = CamionRepositorio.Instance.buscarPorId(ruta.getCamion().getId());
      camionActual.regresarDeposito();
      ruta.asignarCamion(camion);
    } else if (dto.getEstado() != null) {
      switch (dto.getEstado()) {
        case CANCELADA -> ruta.indicarImprovistoLogistico(); // unica razon por la que se cancela
        case EN_CURSO -> ruta.iniciarRuta();
        case FINALIZADA -> ruta.finalizarRuta();
        case NO_INICIADA -> throw new IllegalArgumentException(
            "No se permite regresar el estado de una ruta a NO_INICIADA");
      }
    }
    commit(transaccionPropia);
    return ruta;
  }

  public void eliminarRutaYEntregas(Ruta ruta) {
    EntityTransaction transaction = entityManager().getTransaction();
    boolean transaccionPropia = iniciarTransaccion();
    ruta.getCamion().regresarDeposito();
    ruta.getEntregas().forEach(EntregaRepositorio.Instance::eliminarEntrega);
    eliminar(ruta);
    commit(transaccionPropia);
  }

}