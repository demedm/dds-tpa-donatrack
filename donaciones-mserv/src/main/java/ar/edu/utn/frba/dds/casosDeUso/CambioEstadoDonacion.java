package ar.edu.utn.frba.dds.casosDeUso;

import ar.edu.utn.frba.dds.dto.CambioEstadoDTO;
import ar.edu.utn.frba.dds.exceptions.DonacionInexistenteException;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.notificaciones.Destinatario;
import ar.edu.utn.frba.dds.model.notificaciones.EventoNotificacionService;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;
import ar.edu.utn.frba.dds.repositories.EntidadRepository;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.ArrayList;
import java.util.List;


public class CambioEstadoDonacion implements WithSimplePersistenceUnit {

  public static final CambioEstadoDonacion Instance =
      new CambioEstadoDonacion(EventoNotificacionService.Instance);

  private final EventoNotificacionService notificaciones;

  public CambioEstadoDonacion(EventoNotificacionService notificaciones) {
    this.notificaciones = notificaciones;
  }

  public void aplicar(Long donacionId, CambioEstadoDTO cambio) {
    DonacionSegmentada segmentada =
        DonacionesRepository.Instance.findSegmentadaById(donacionId);

    if (segmentada == null) {
      throw new DonacionInexistenteException("No existe la donacion " + donacionId);
    }

    withTransaction(() -> {
      if (tieneTexto(cambio.getMotivoFalla())) {
        segmentada.setJustificacionFallo(cambio.getMotivoFalla());
      }
      segmentada.cambiarEstado(cambio.getNuevoEstado());
    });

    notificar(segmentada, donacionId, cambio);
  }

  private void notificar(DonacionSegmentada segmentada,
                         Long donacionId,
                         CambioEstadoDTO cambio) {

    List<Destinatario> involucrados = involucradosEn(segmentada);

    if (involucrados.isEmpty()) {
      System.err.println("[CambioEstado] La donacion " + donacionId
          + " no tiene donante ni entidad resolubles: no se notifica a nadie.");
      return;
    }

    switch (cambio.getNuevoEstado().toUpperCase()) {

      case "EN_TRASLADO" -> notificaciones.notificarInicioRuta(
          involucrados,
          tieneTexto(cambio.getRutaId()) ? cambio.getRutaId() : String.valueOf(donacionId),
          cambio.getUrlMapa());

      case "ENTREGADA" -> notificaciones.notificarEntregaExitosa(
          involucrados,
          donacionId,
          cambio.getFechaHora(),
          cambio.getPatenteCamion());

      case "FALLIDA", "NO_RECIBIDA" -> notificaciones.notificarEntregaFallida(
          involucrados,
          donacionId,
          cambio.getMotivoFalla(),
          cambio.isReplanificable());

      default -> {
      }
    }
  }

  private List<Destinatario> involucradosEn(DonacionSegmentada segmentada) {
    List<Destinatario> involucrados = new ArrayList<>();

    Persona donante = segmentada.getDonante();
    if (donante != null) {
      involucrados.add(donante);
    }

    Long entidadId = segmentada.getEntidadAsignadaId();
    if (entidadId != null) {
      EntidadBeneficiaria entidad = EntidadRepository.Instance.obtenerPorId(entidadId);
      if (entidad != null) {
        involucrados.add(entidad);
      }
    }

    return involucrados;
  }

  private boolean tieneTexto(String valor) {
    return valor != null && !valor.isBlank();
  }
}
