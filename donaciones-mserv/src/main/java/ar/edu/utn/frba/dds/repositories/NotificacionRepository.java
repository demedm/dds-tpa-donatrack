package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.notificaciones.Notificacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Historial de notificaciones enviadas por el sistema, en memoria.*/

public class NotificacionRepository {
  public static NotificacionRepository Instance = new NotificacionRepository();

  private final List<Notificacion> historial = Collections.synchronizedList(new ArrayList<>());

  public void registrar(Notificacion notificacion) {
    historial.add(notificacion);
  }

  public List<Notificacion> obtenerHistorial() {
    synchronized (historial) {
      return new ArrayList<>(historial);
    }
  }
}