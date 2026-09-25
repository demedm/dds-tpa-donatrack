package ar.edu.utn.frba.dds.tareas;

import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.notificaciones.EventoNotificacionService;
import ar.edu.utn.frba.dds.repositories.DonanteRepository;
import ar.edu.utn.frba.dds.model.notificaciones.TipoEvento;
import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;
import ar.edu.utn.frba.dds.repositories.NotificacionRepository;

import java.time.LocalDate;
import java.util.List;

public class NotificarInactivos {

  public static int ejecutar(int dias) {
    LocalDate hoy = LocalDate.now();
    LocalDate limite = hoy.minusDays(dias);

    List<Persona> inactivos =
        DonanteRepository.Instance.buscarInactivosDesde(limite);

    int notificados = 0;

    for (Persona donante : inactivos) {
      if (yaLeAvisamos(donante, limite)) {
        continue;
      }
      try {
        EventoNotificacionService.Instance.notificarInactividadDonante(donante);
        notificados++;
      } catch (Exception e) {
        System.err.println("[Inactivos] No se pudo notificar a "
            + donante.getNombreIdentificador() + ": " + e.getMessage());
      }
    }
    return notificados;
  }

  private static boolean yaLeAvisamos(Persona donante, LocalDate limite) {
    MedioContacto medio = donante.medioDeContacto();
    if (medio == null) {
      return false;
    }
    return NotificacionRepository.Instance.yaSeNotifico(
        medio.getMedioContacto(),
        TipoEvento.INACTIVIDAD_DONANTE,
        limite.atStartOfDay());
  }
}