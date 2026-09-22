// ar/edu/utn/frba/dds/controllers/EstadoEntregaController.java   [NUEVO]
package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.CambioEstadoDTO;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.notificaciones.EventoNotificacionService;
import ar.edu.utn.frba.dds.model.notificaciones.Destinatario;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;
import ar.edu.utn.frba.dds.repositories.DonanteRepository;
import ar.edu.utn.frba.dds.repositories.EntidadRepository;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Recibe los cambios de estado que informa el microservicio de logistica y
 * dispara las notificaciones correspondientes.
 *
 * ESTE ENDPOINT NO EXISTIA. El Client de logistica ya llamaba a
 * PUT /donaciones/{id}/estado pero donaciones no lo exponia, asi que la
 * integracion de los eventos 4, 5 y 6 estaba cortada.
 *
 * Las notificaciones viven SOLO aca: logistica no manda mails ni SMS, avisa por
 * REST. Donaciones es quien conoce a los donantes, a las entidades y sus medios
 * de contacto; duplicar Twilio y SMTP en los dos servicios seria peor.
 */
public class EstadoEntregaController {

  private final EventoNotificacionService notificaciones;

  public EstadoEntregaController() {
    this(EventoNotificacionService.Instance);
  }

  public EstadoEntregaController(EventoNotificacionService notificaciones) {
    this.notificaciones = notificaciones;
  }

  public void cambiarEstado(Context context) {
    String segmentadaId = context.pathParam("id");
    CambioEstadoDTO cambio = context.bodyAsClass(CambioEstadoDTO.class);

    DonacionSegmentada segmentada =
        DonacionesRepository.Instance.findSegmentadaById(segmentadaId);

    if (segmentada == null) {
      context.status(404).result("No existe la donacion " + segmentadaId);
      return;
    }

    segmentada.cambiarEstado(cambio.getNuevoEstado());

    List<Destinatario> involucrados = involucradosEn(segmentada);

    switch (cambio.getNuevoEstado().toUpperCase()) {

      case "EN_TRASLADO" -> notificaciones.notificarInicioRuta(
          involucrados,
          cambio.getRutaId() != null ? cambio.getRutaId() : segmentadaId,
          cambio.getUrlMapa());

      case "ENTREGADA" -> notificaciones.notificarEntregaExitosa(
          involucrados,
          segmentadaId,
          cambio.getFechaHora(),
          cambio.getPatenteCamion());

      case "FALLIDA", "NO_RECIBIDA" -> {
        segmentada.setJustificacionFallo(cambio.getMotivoFalla());
        notificaciones.notificarEntregaFallida(
            involucrados,
            segmentadaId,
            cambio.getMotivoFalla(),
            cambio.isReplanificable());
      }

      default -> {
        // Otros cambios de estado no disparan notificaciones.
      }
    }

    context.status(200).json(cambio);
  }

  /**
   * El donante y la entidad beneficiaria de esta donacion. Si alguno no se
   * puede resolver, se notifica igual al otro.
   */
  private List<Destinatario> involucradosEn(DonacionSegmentada segmentada) {
    List<Destinatario> involucrados = new ArrayList<>();

    Optional<Persona> donante =
        DonanteRepository.Instance.buscarPorEmail(segmentada.getDonanteEmail());
    donante.ifPresent(involucrados::add);

    String entidadId = segmentada.getEntidadAsignadaId();
    if (entidadId != null) {
      EntidadBeneficiaria entidad = EntidadRepository.Instance.obtenerPorId(entidadId);
      if (entidad != null) {
        involucrados.add(entidad);
      }
    }

    return involucrados;
  }
}