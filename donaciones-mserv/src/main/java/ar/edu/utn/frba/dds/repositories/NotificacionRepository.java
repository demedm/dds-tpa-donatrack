package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.notificaciones.Notificacion;
import ar.edu.utn.frba.dds.model.notificaciones.TipoEvento;
import ar.edu.utn.frba.dds.model.notificaciones.EstadoNotificacion;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Historial de notificaciones enviadas por el sistema, en memoria.*/

public class NotificacionRepository {
  public static NotificacionRepository Instance = new NotificacionRepository();

  private EntityManager em() {
    return EntityManagerHelper.getEntityManager();
  }

  public void registrar(Notificacion notificacion) {
    EntityManagerHelper.beginTransaction();
    try {
      em().persist(notificacion);
      EntityManagerHelper.commit();
    } catch (RuntimeException e) {
      EntityManagerHelper.rollback();
      throw e;
    }
  }
  // Cambia los estados de la notificacion luego de presistirse
  public void actualizar(Notificacion notificacion) {
    EntityManagerHelper.beginTransaction();
    try {
      em().merge(notificacion);
      EntityManagerHelper.commit();
    } catch (RuntimeException e) {
      EntityManagerHelper.rollback();
      throw e;
    }
  }

  public List<Notificacion> obtenerHistorial() {
    return em()
        .createQuery("FROM Notificacion ORDER BY fechaHora DESC", Notificacion.class)
        .getResultList();
  }

  //Evitamos renotificar todos los dias al mismo destinatario por el moismo motivo
  public boolean yaSeNotifico(String destinatario, TipoEvento tipo, LocalDateTime desde) {
    if (destinatario == null) {
      return false;
    }
    Long cantidad = em()
        .createQuery(
            "SELECT COUNT(n) FROM Notificacion n "
                + "WHERE n.destinatario = :destinatario "
                + "AND n.tipoEvento = :tipo "
                + "AND n.estado = :estado "
                + "AND n.fechaHora > :desde", Long.class)
        .setParameter("destinatario", destinatario)
        .setParameter("tipo", tipo)
        .setParameter("estado", EstadoNotificacion.COMPLETADA)
        .setParameter("desde", desde)
        .getSingleResult();

    return cantidad > 0;
  }
}