package ar.edu.utn.frba.dds.model.notificaciones;

import ar.edu.utn.frba.dds.repositories.AdministradorRepository;
import java.util.List;

/**
 * Traduce un evento del dominio al texto de la notificacion y la manda.
 *
 * Un metodo por evento (antes habia dos sobrecargas de varios: una para Persona
 * y otra para MedioContacto, con el mismo texto duplicado).
 *
 * Los eventos con MULTIPLES destinatarios reciben una lista, porque el enunciado
 * pide notificar "a todas las entidades beneficiarias y a los donantes cuyas
 * entregas formen parte de la ruta" y, en la entrega fallida, tambien a las
 * personas administradoras.
 */
public class EventoNotificacionService {

  public static final EventoNotificacionService Instance =
      new EventoNotificacionService(new NotificadorAsincronico());

  private final EnviadorNotificaciones enviador;

  public EventoNotificacionService(EnviadorNotificaciones enviador) {
    this.enviador = enviador;
  }

  // ---------- 1. Ausencia de la plataforma ----------

  public Notificacion notificarInactividadDonante(Destinatario donante) {
    return enviar(donante, String.format(
        "Hola %s, te extranamos en DonaTrack. Pasaron mas de 20 dias desde tu "
            + "ultima actividad. Queres realizar una nueva donacion?",
        donante.nombreParaMostrar()));
  }

  // ---------- 2 y 3. Donacion asignada ----------

  public Notificacion notificarDonacionAsignadaDonante(Destinatario donante,
                                                       String donacionId,
                                                       String nombreEntidad) {
    return enviar(donante, String.format(
        "Hola %s, tu donacion (ID: %s) fue asignada a %s. Gracias por tu generosidad!",
        donante.nombreParaMostrar(), donacionId, nombreEntidad));
  }

  /**
   * nombreEntidad va como parametro y no sale de nombreParaMostrar() porque el
   * destinatario podria ser un MedioContacto suelto, y ahi devolveria el mail.
   */
  public Notificacion notificarDonacionAsignadaBeneficiario(Destinatario beneficiario,
                                                            String nombreEntidad,
                                                            String donacionId) {
    return enviar(beneficiario, String.format(
        "Estimada/o %s, se les ha asignado una donacion (ID: %s) acorde a sus "
            + "necesidades registradas. Proximamente recibiran informacion sobre la entrega.",
        nombreEntidad, donacionId));
  }

  // ---------- 4. Inicio de ruta ----------

  /**
   * El enunciado pide notificar a TODAS las entidades beneficiarias y a los
   * donantes de la ruta, con enlace al mapa interactivo.
   */
  public void notificarInicioRuta(List<Destinatario> destinatarios,
                                  String rutaId,
                                  String urlMapa) {
    String enlace = (urlMapa != null && !urlMapa.isBlank())
        ? urlMapa
        : "disponible proximamente en la plataforma";

    String mensaje = String.format(
        "La ruta de entrega %s ha comenzado. Podes seguir el camion en tiempo real aqui: %s",
        rutaId, enlace);

    destinatarios.forEach(destinatario -> enviarSeguro(destinatario, mensaje));
  }

  // ---------- 5. Entrega exitosa ----------

  /** Comprobante de entrega: fecha, hora y camion responsable. */
  public void notificarEntregaExitosa(List<Destinatario> destinatarios,
                                      String donacionId,
                                      String fechaHora,
                                      String patenteCamion) {
    String mensaje = String.format(
        "La donacion (ID: %s) fue entregada exitosamente.%n"
            + "Comprobante de entrega:%n"
            + "  Fecha y hora: %s%n"
            + "  Camion: %s%n"
            + "Muchas gracias!",
        donacionId,
        fechaHora != null ? fechaHora : "no informada",
        patenteCamion != null ? patenteCamion : "no informado");

    destinatarios.forEach(destinatario -> enviarSeguro(destinatario, mensaje));
  }

  // ---------- 6. Entrega no satisfactoria ----------

  /**
   * Va a la entidad, al donante Y a las personas administradoras.
   * Los admins salen del repositorio, no de System.getenv dentro del dominio.
   */
  public void notificarEntregaFallida(List<Destinatario> destinatarios,
                                      String donacionId,
                                      String motivo,
                                      boolean replanificable) {
    String motivoTexto = (motivo != null && !motivo.isBlank()) ? motivo : "no especificado";

    String mensajeInvolucrados = String.format(
        "La entrega de la donacion (ID: %s) no pudo concretarse. Motivo: %s.%s",
        donacionId, motivoTexto,
        replanificable
            ? " La entrega sera replanificada."
            : " El equipo administrativo revisara el caso.");

    destinatarios.forEach(destinatario -> enviarSeguro(destinatario, mensajeInvolucrados));

    String mensajeAdmins = String.format(
        "[ADMIN] Entrega fallida. Donacion: %s | Motivo: %s | Replanificable: %s",
        donacionId, motivoTexto, replanificable ? "si" : "no");

    AdministradorRepository.Instance.administradores()
        .forEach(admin -> enviarSeguro(admin, mensajeAdmins));
  }

  // ---------- helpers ----------

  private Notificacion enviar(Destinatario destinatario, String mensaje) {
    return enviador.enviarNotificacionA(destinatario, mensaje);
  }

  /**
   * En los eventos con varios destinatarios, que uno no tenga medio de contacto
   * no puede impedir que los demas se enteren.
   */
  private void enviarSeguro(Destinatario destinatario, String mensaje) {
    try {
      enviador.enviarNotificacionA(destinatario, mensaje);
    } catch (Exception e) {
      System.err.println("[Notificaciones] No se pudo notificar a "
          + destinatario.nombreParaMostrar() + ": " + e.getMessage());
    }
  }
}