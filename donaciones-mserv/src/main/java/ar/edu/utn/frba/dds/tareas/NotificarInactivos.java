package ar.edu.utn.frba.dds.tareas;

import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.notificaciones.EventoNotificacionService;
import ar.edu.utn.frba.dds.repositories.DonanteRepository;

import java.time.LocalDate;
import java.util.List;

public class NotificarInactivos {

  public static int ejecutar(int dias) {
    List<Persona> inactivos =
        DonanteRepository.Instance.buscarInactivosDesde(LocalDate.now().minusDays(dias));

    inactivos.forEach(donante -> {
      try {
        EventoNotificacionService.Instance.notificarInactividadDonante(donante);
      } catch (Exception e) {
        System.err.println("[Inactivos] No se pudo notificar a "
            + donante.getNombreIdentificador() + ": " + e.getMessage());
      }
    });

    return inactivos.size();
  }
}