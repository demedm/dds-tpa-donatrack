package ar.edu.utn.frba.dds.repositories.planificacion;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.EstadoCamion;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.model.usuarios.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.planificacion.EstadoItem;
import ar.edu.utn.frba.dds.planificacion.EstadoLote;
import ar.edu.utn.frba.dds.planificacion.ItemPlanificacion;
import ar.edu.utn.frba.dds.planificacion.LotePlanificacion;
import ar.edu.utn.frba.dds.planificacion.ResultadoPlanificacion;
import ar.edu.utn.frba.dds.scripts.dto.planificacion.CamionDisponibleDto;
import ar.edu.utn.frba.dds.scripts.dto.planificacion.DestinoDto;
import ar.edu.utn.frba.dds.scripts.dto.planificacion.DonacionAPlanificarDto;
import ar.edu.utn.frba.dds.scripts.dto.planificacion.ResponsePlanificacionDto;
import ar.edu.utn.frba.dds.scripts.dto.planificacion.RutaPlanificadaDto;
import ar.edu.utn.frba.dds.scripts.dto.planificacion.SolicitudPlanificacionDto;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

public class PlanificacionRepositorio implements WithSimplePersistenceUnit {
  public static final PlanificacionRepositorio Instance = new PlanificacionRepositorio();
  private static final int MAX_INTENTOS = 3; // de planificacion

  public void registrar(ItemPlanificacion item) {
    withTransaction(()->entityManager().persist(item));
  }

  public void eliminar(ItemPlanificacion item) {
    withTransaction(()->entityManager().remove(item));
  }

  public ItemPlanificacion actualizar(ItemPlanificacion item) {
    return withTransaction(()->entityManager().merge(item));
  }

  public void registrar(LotePlanificacion lote) {
    withTransaction(()->entityManager().persist(lote));
  }

  public void eliminar(LotePlanificacion lote) {
    withTransaction(()->entityManager().remove(lote));
  }

  public LotePlanificacion actualizar(LotePlanificacion lote) {
    return withTransaction(()->entityManager().merge(lote));
  }

  public boolean existeDonacion(Long id) {
    try {
      var item = entityManager()
          .createQuery("SELECT i FROM ItemPlanificacion i WHERE i.donacionId = :id", ItemPlanificacion.class
          )
          .setParameter("id", id)
          .getResultStream()
          .findFirst()
          .orElse(null);
      return item != null;
    } catch (Exception e) {
      System.err.println(e.getMessage());
      return false;
    }
  }

  public SolicitudPlanificacionDto prepararSolicitud(int TAMANIO_LOTE, String CALLBACK_URL) {
    Long enviados = entityManager()
        .createQuery("select count(l) from LotePlanificacion l where l.estado = :e", Long.class)
        .setParameter("e", EstadoLote.ENVIADO).getSingleResult();
    if (enviados > 0) return null;   // ya hay un lote esperando respuesta

    List<ItemPlanificacion> items = entityManager()
        .createQuery("from ItemPlanificacion i where i.estado = :e order by i.fechaVencimiento",
            ItemPlanificacion.class)
        .setParameter("e", EstadoItem.PENDIENTE)
        .setMaxResults(TAMANIO_LOTE)
        .getResultList();
    if (items.isEmpty()) return null;

    List<Camion> camiones = camionesDisponibles();   // ver nota 2
    if (camiones.isEmpty()) return null;

    LotePlanificacion lote = new LotePlanificacion();
    lote.setEstado(EstadoLote.ENVIADO);
    lote.setIdsCamiones(camiones.stream().map(Camion::getId).toList());
    entityManager().persist(lote);   // asigna el id

    LocalDateTime ahora = LocalDateTime.now();
    for (ItemPlanificacion i : items) {
      i.enviarAPlanificar(lote);
      i.setEnviadoEn(ahora);
    }

    return new SolicitudPlanificacionDto(
        lote.getId(), CALLBACK_URL,
        items.stream().map(DonacionAPlanificarDto::de).toList(),
        camiones.stream().map(CamionDisponibleDto::de).toList());
  }

  /*
  public void revertirLote(Long loteId) {
    LotePlanificacion lote = entityManager().find(LotePlanificacion.class, loteId);
    lote.setEstado(EstadoLote.FALLIDO);
    entityManager()
        .createQuery("from ItemPlanificacion i where i.lote.id = :id", ItemPlanificacion.class)
        .setParameter("id", loteId).getResultList()
        .forEach(i -> {
          i.setLote(null);
          i.setEstado(EstadoItem.PENDIENTE);   // sin sumar intentos: no fue culpa de la donación
        });
  }
  */

  public void revertirLote(Long loteId) {
    withTransaction(() -> {
      LotePlanificacion lote =
          entityManager().find(LotePlanificacion.class, loteId);

      if (lote == null) {
        throw new IllegalArgumentException(
            "No existe el lote " + loteId
        );
      }

      lote.setEstado(EstadoLote.FALLIDO);

      entityManager()
          .createQuery(
              "from ItemPlanificacion i where i.lote.id = :id",
              ItemPlanificacion.class
          )
          .setParameter("id", loteId)
          .getResultList()
          .forEach(i -> {
            i.setLote(null);
            i.setEstado(EstadoItem.PENDIENTE);
          });
    });
  }

  public List<Camion> camionesDisponibles() {
    // ajustá al nombre real de tu enum y a la regla de disponibilidad
    return entityManager()
        .createQuery("from Camion c where c.estado = :e", Camion.class)
        .setParameter("e", EstadoCamion.DISPONIBLE)
        .getResultList();
  }

  public ResultadoPlanificacion procesarRespuesta(ResponsePlanificacionDto resp) {
    return withTransaction(() -> {
      ResultadoPlanificacion res = new ResultadoPlanificacion();

      if (resp.getLoteId() == null) {
        throw new IllegalArgumentException("Falta loteId");
      }
      LotePlanificacion lote = entityManager().find(LotePlanificacion.class, resp.getLoteId());
      if (lote == null) {
        throw new NoSuchElementException("Lote inexistente: " + resp.getLoteId());
      }

      // Idempotencia: solo se procesa un lote ENVIADO. Repetidos, FALLIDO o EXPIRADO se ignoran.
      if (lote.getEstado() != EstadoLote.ENVIADO) {
        res.yaProcesado = true;
        return res;
      }

      Map<Long, ItemPlanificacion> items = entityManager()
          .createQuery("from ItemPlanificacion i where i.lote.id = :id", ItemPlanificacion.class)
          .setParameter("id", lote.getId()).getResultList().stream()
          .collect(Collectors.toMap(ItemPlanificacion::getDonacionId, i -> i));

      // ---- Validación (si algo no cierra, se lanza y se hace rollback) ----
      Set<Long> camionesUsados = new HashSet<>();
      Set<Long> donacionesVistas = new HashSet<>();
      for (RutaPlanificadaDto rp : resp.getRutasPlanificadas()) {
        if (!lote.getIdsCamiones().contains(rp.getCamionId())) {
          throw new IllegalArgumentException("Camión no ofrecido en el lote: " + rp.getCamionId());
        }
        if (!camionesUsados.add(rp.getCamionId())) {
          throw new IllegalArgumentException("Camión repetido: " + rp.getCamionId());
        }
        for (DestinoDto d : rp.getDestinos()) {
          for (Long id : d.getDonacionIds()) {
            if (!items.containsKey(id)) {
              throw new IllegalArgumentException("Donación ajena al lote: " + id);
            }
            if (!donacionesVistas.add(id)) {
              throw new IllegalArgumentException("Donación asignada dos veces: " + id);
            }
          }
        }
      }
      for (Long id : resp.getDonacionesNoPlanificadas()) {
        if (!items.containsKey(id)) {
          throw new IllegalArgumentException("Donación ajena al lote: " + id);
        }
        if (donacionesVistas.contains(id)) {
          throw new IllegalArgumentException("Donación asignada y no asignada a la vez: " + id);
        }
      }

      // ---- Rutas asignadas ----
      for (RutaPlanificadaDto rp : resp.getRutasPlanificadas()) {
        Camion camion = entityManager().find(Camion.class, rp.getCamionId());
        List<Entrega> entregas = new ArrayList<>();

        int orden = 0;
        for (DestinoDto d : rp.getDestinos()) {
          orden++;                                   // parada 1, 2, 3...
          for (Long id : d.getDonacionIds()) {
            ItemPlanificacion item = items.get(id);

            Entrega e = new Entrega(d.getDireccion(), id);
            e.setOrdenParada(orden);
            e.setFechaVencimiento(item.getFechaVencimiento());
            e.setEntidadBeneficiaria(
                entityManager().find(EntidadBeneficiaria.class, item.getBeneficiarioId()));
            entregas.add(e);

            item.asignar();
            res.asignadas.add(id);
          }
        }

        Ruta ruta = new Ruta(entregas);
        ruta.asignarCamion(camion);
        camion.asignarRuta();
        entityManager().persist(ruta);
      }

      // ---- No asignadas (informadas o ausentes en la respuesta) ----
      boolean huboAsignadas = !res.asignadas.isEmpty();
      for (ItemPlanificacion item : items.values()) {
        if (item.getEstado() != EstadoItem.EN_PLANIFICACION) continue;   // ya asignado

        item.devolverAPendiente();                                       // intentosPlanif++
        boolean sinFuturo = !huboAsignadas || item.getIntentosPlanif() >= MAX_INTENTOS;
        if (sinFuturo) {
          item.marcarNoPlanificable();
          res.noPlanificables.add(item.getDonacionId());
        } else {
          res.reintentables.add(item.getDonacionId());
        }
      }

      lote.setEstado(EstadoLote.RECIBIDO);
      lote.setRecibidoEn(LocalDateTime.now());
      return res;
    });
  }

  public SolicitudPlanificacionDto prepararSolicitudManual(
      List<Long> camionesIds,
      int tamanioLote,
      String callbackUrl) {

    return withTransaction(() -> {

      if (camionesIds == null || camionesIds.isEmpty()) {
        throw new IllegalArgumentException(
            "Debe seleccionar al menos un camión."
        );
      }

      if (new HashSet<>(camionesIds).size() != camionesIds.size()) {
        throw new IllegalArgumentException(
            "No se permiten camiones repetidos."
        );
      }

      // Comprobar que no haya otro lote esperando respuesta.
      Long enviados = entityManager()
          .createQuery(
              "select count(l) from LotePlanificacion l where l.estado = :e",
              Long.class
          )
          .setParameter("e", EstadoLote.ENVIADO)
          .getSingleResult();

      if (enviados > 0) {
        throw new IllegalStateException(
            "Ya existe una planificación esperando respuesta."
        );
      }

      // Buscar exclusivamente los camiones seleccionados.
      List<Camion> camiones = entityManager()
          .createQuery(
              "from Camion c where c.id in :ids",
              Camion.class
          )
          .setParameter("ids", camionesIds)
          .getResultList();

      if (camiones.size() != camionesIds.size()) {
        throw new IllegalArgumentException(
            "Uno o más camiones seleccionados no existen."
        );
      }

      // Verificar disponibilidad.
      List<Long> noDisponibles = camiones.stream()
          .filter(c -> c.getEstado() != EstadoCamion.DISPONIBLE)
          .map(Camion::getId)
          .toList();

      if (!noDisponibles.isEmpty()) {
        throw new IllegalStateException(
            "Los siguientes camiones no están disponibles: "
                + noDisponibles
        );
      }

      // Obtener donaciones pendientes.
      List<ItemPlanificacion> items = entityManager()
          .createQuery(
              "from ItemPlanificacion i " +
                  "where i.estado = :e " +
                  "order by i.fechaVencimiento",
              ItemPlanificacion.class
          )
          .setParameter("e", EstadoItem.PENDIENTE)
          .setMaxResults(tamanioLote)
          .getResultList();

      if (items.isEmpty()) {
        throw new IllegalStateException(
            "No hay donaciones pendientes de planificación."
        );
      }

      // Crear lote con los camiones seleccionados.
      LotePlanificacion lote = new LotePlanificacion();
      lote.setEstado(EstadoLote.ENVIADO);
      lote.setIdsCamiones(
          camiones.stream().map(Camion::getId).toList()
      );

      entityManager().persist(lote);
      entityManager().flush();

      // Marcar donaciones como enviadas.
      LocalDateTime ahora = LocalDateTime.now();

      for (ItemPlanificacion item : items) {
        item.enviarAPlanificar(lote);
        item.setEnviadoEn(ahora);
      }

      return new SolicitudPlanificacionDto(
          lote.getId(),
          callbackUrl,
          items.stream()
              .map(DonacionAPlanificarDto::de)
              .toList(),
          camiones.stream()
              .map(CamionDisponibleDto::de)
              .toList()
      );
    });
  }

}
