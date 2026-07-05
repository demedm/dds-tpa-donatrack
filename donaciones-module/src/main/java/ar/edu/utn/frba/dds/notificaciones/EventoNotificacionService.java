package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.donantes.Persona;
import ar.edu.utn.frba.dds.medioscontacto.MedioContacto;
import org.springframework.stereotype.Service;

/**
 * Construye el mensaje apropiado para cada evento del sistema y delega
 * el envío al Notificador existente.
 *
 * Separa "qué decir" (lógica de negocio) de "cómo enviarlo" (Notificador + MedioContacto).
 * El Notificador usa el medio preferido de cada Persona — no es necesario pasarlo explícitamente.
 *
 * Para entidades beneficiarias (que no extienden Persona), se recibe el MedioContacto
 * directamente ya que EntidadBeneficiaria no hereda de Persona.
 */
@Service
public class EventoNotificacionService {

  private final Notificador notificador;

  public EventoNotificacionService(Notificador notificador) {
    this.notificador = notificador;
  }

  /** Donante sin actividad por más de 20 días. */
  public Notificacion notificarInactividadDonante(Persona donante) {
    String mensaje = String.format(
        "Hola %s, te extrañamos en DonaTrack. "
            + "Han pasado más de 20 días desde tu última actividad. "
            + "¿Querés realizar una nueva donación? Ingresá a la plataforma.",
        donante.getNombreIdentificador());

    return notificador.enviarNotificacionA(donante, mensaje);
  }

  /** Notificación al donante cuando su donación fue asignada. */
  public Notificacion notificarDonacionAsignadaDonante(Persona donante,
                                                       String donacionId, String nombreEntidad) {
    String mensaje = String.format(
        "Hola %s, tu donación (ID: %s) fue asignada a %s. ¡Gracias por tu generosidad!",
        donante.getNombreIdentificador(), donacionId, nombreEntidad);

    return notificador.enviarNotificacionA(donante, mensaje);
  }

  /**
   * Notificación a una entidad beneficiaria cuando se le asigna una donación.
   * Se pasa el MedioContacto directamente porque EntidadBeneficiaria no es Persona.
   */
  public Notificacion notificarDonacionAsignadaBeneficiario(MedioContacto medioEntidad,
                                                            String nombreEntidad, String donacionId) {
    Notificacion notificacion = new Notificacion(
        String.format(
            "Estimada/o %s, se les ha asignado una donación (ID: %s) "
                + "acorde a sus necesidades registradas. "
                + "Próximamente recibirán información sobre la entrega.",
            nombreEntidad, donacionId));

    medioEntidad.contactar(notificacion);
    return notificacion;
  }

  /** Inicio de ruta — incluye URL del mapa en tiempo real. */
  public Notificacion notificarInicioRuta(Persona persona, String rutaId, String urlMapa) {
    String mensaje = String.format(
        "La ruta de entrega %s ha comenzado. "
            + "Podés seguir el camión en tiempo real aquí: %s",
        rutaId, urlMapa);

    return notificador.enviarNotificacionA(persona, mensaje);
  }

  /** Entrega confirmada exitosamente — incluye comprobante. */
  public Notificacion notificarEntregaExitosa(Persona persona,
                                              String donacionId, String fechaHora, String patenteCamion) {
    String mensaje = String.format(
        "La donación (ID: %s) fue entregada exitosamente. "
            + "Fecha y hora: %s | Camión: %s. ¡Muchas gracias!",
        donacionId, fechaHora, patenteCamion);

    return notificador.enviarNotificacionA(persona, mensaje);
  }

  /** Entrega fallida — para destinatarios que son Persona (donante o representante). */
  public Notificacion notificarEntregaFallida(Persona persona,
                                              String donacionId, String motivo) {
    String mensaje = String.format(
        "La entrega de la donación (ID: %s) no pudo concretarse. "
            + "Motivo: %s. El equipo administrativo revisará el caso "
            + "y coordinará una nueva asignación si corresponde.",
        donacionId, motivo);

    return notificador.enviarNotificacionA(persona, mensaje);
  }

  /** Entrega fallida — para cuando el destinatario no es una Persona (entidad directa). */
  public Notificacion notificarEntregaFallidaMedio(MedioContacto medio,
                                                   String donacionId, String motivo) {
    Notificacion notificacion = new Notificacion(
        String.format(
            "La entrega de la donación (ID: %s) no pudo concretarse. "
                + "Motivo: %s. El equipo administrativo revisará el caso.",
            donacionId, motivo));

    medio.contactar(notificacion);
    return notificacion;
  }
}
