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
import javax.persistence.EntityManager;
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

  public Ruta iniciarRuta(Chofer chofer, Long idRuta) {
    return withTransaction(() -> {
      Ruta ruta = buscarPorId(idRuta);
      if (ruta.getChofer() == null || !Objects.equals(chofer.getId(), ruta.getChofer().getId())) {
        return null;
      }
      ruta.iniciarRuta();
      observers.forEach(observer -> observer.actualizarRuta(ruta, true));
      ruta.getEntregas().forEach(EntregaRepositorio.Instance::notificarInicioDeEntrega);
      return ruta;
    });
  }

  public Ruta registrar(Ruta ruta) {
    return withTransaction(() -> entityManager().merge(ruta));
  }

  public void eliminarRuta(Ruta ruta) {
    withTransaction(() -> entityManager().remove(ruta));
  }

  private void eliminar(Ruta ruta) {
    entityManager().remove(ruta);
  }

  @SuppressWarnings("unchecked")
  public List<Ruta> mostrarTodos() {
    return entityManager()
            .createQuery("from Ruta", Ruta.class)
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
    Ruta ruta = entityManager().find(Ruta.class, id);
    if (ruta == null) {
      throw new RutaNotFoundException(id);
    }
    return ruta;
  }

  @SuppressWarnings("unchecked")
  public List<Ruta> buscarRutasDeCamion(Long idCamion) {
    return entityManager()
            .createQuery("from Ruta r where r.camion.id = :id", Ruta.class)
            .setParameter("id", idCamion)
            .getResultList();
  }

  public Ruta buscarRutaEnCursoDeCamion(Long idCamion) {
    List<Ruta> resultados = entityManager()
            .createQuery("from Ruta r where r.camion.id = :id and r.estado = :estado", Ruta.class)
            .setParameter("id", idCamion)
            .setParameter("estado", EstadoRuta.EN_CURSO)
            .getResultList();

    return resultados.isEmpty() ? null : resultados.get(0);
  }

  @SuppressWarnings("unchecked")
  public Ruta buscarRutasDeCamionConEstado(Long idCamion, EstadoRuta estadoRuta) {
    List<Ruta> resultados = entityManager()
            .createQuery("from Ruta r where r.camion.id = :id and r.estado = :estado", Ruta.class)
            .setParameter("id", idCamion)
            .setParameter("estado", estadoRuta)
            .getResultList();

    return resultados.isEmpty() ? null : resultados.get(0);
  }

  @SuppressWarnings("unchecked")
  public List<Ruta> buscarRutasDeChofer(Long idChofer) {
    return entityManager()
            .createQuery("from Ruta r where r.chofer.id = :id", Ruta.class)
            .setParameter("id", idChofer)
            .getResultList();
  }

  @SuppressWarnings("unchecked")
  public List<Ruta> buscarRutasConEstado(EstadoRuta estado) {
    return entityManager()
            .createQuery("from Ruta r where r.estado = :estado", Ruta.class)
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
    rutas.forEach(this::registrar);
  }

  public Ruta actualizarRuta(Ruta ruta, RutaDto dto) {
    return withTransaction(() -> {
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
          case CANCELADA -> ruta.indicarImprevistoLogistico();
          case EN_CURSO -> ruta.iniciarRuta();
          case FINALIZADA -> ruta.finalizarRuta();
          case NO_INICIADA -> throw new IllegalArgumentException(
                  "No se permite regresar el estado de una ruta a NO_INICIADA");
        }
      }
      return entityManager().merge(ruta);
    });
  }

  public void eliminarRutaYEntregas(Ruta ruta) {
    withTransaction(() -> {
      if (ruta.getCamion() != null) {
        ruta.getCamion().regresarDeposito();
      }
      ruta.getEntregas().forEach(EntregaRepositorio.Instance::eliminarEntrega);
      entityManager().remove(entityManager().contains(ruta) ? ruta : entityManager().merge(ruta));
    });
  }
  public Ruta buscarRutaDeEntrega(Long idEntrega) {
    List<Ruta> resultados = entityManager()
            .createQuery("select r from Ruta r join r.entregas e where e.id = :idEntrega", Ruta.class)
            .setParameter("idEntrega", idEntrega)
            .getResultList();

    return resultados.isEmpty() ? null : resultados.get(0);
  }
}