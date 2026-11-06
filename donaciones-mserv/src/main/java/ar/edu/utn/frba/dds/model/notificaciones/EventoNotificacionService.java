package ar.edu.utn.frba.dds.model.notificaciones;

import ar.edu.utn.frba.dds.repositories.NotificacionRepository;
import ar.edu.utn.frba.dds.repositories.usuarios.AdministradorRepository;

import java.util.List;

public class EventoNotificacionService {

  public static final EventoNotificacionService Instance =
      new EventoNotificacionService(new NotificadorAsincronico());

  private final EnviadorNotificaciones enviador;

  public EventoNotificacionService(EnviadorNotificaciones enviador) {
    this.enviador = enviador;
  }

  // ---------- 1. Ausencia de la plataforma ----------

  public Notificacion notificarBienvenida(Destinatario donante) {
    return enviar(donante, TipoEvento.BIENVENIDA, String.format(
        "Hola %s, bienvenido/a a DonaTrack! Ya podes registrar tus donaciones.",
        donante.nombreParaMostrar()));
  }

  public Notificacion notificarInactividadDonante(Destinatario donante) {
    return enviar(donante, TipoEvento.INACTIVIDAD_DONANTE, String.format(
        "Hola %s, te extranamos en DonaTrack. Pasaron mas de 20 dias desde tu "
            + "ultima actividad. Queres realizar una nueva donacion?",
        donante.nombreParaMostrar()));
  }

  // ---------- 2 y 3. Donacion asignada ----------

  public Notificacion notificarDonacionAsignadaDonante(Destinatario donante,
                                                       Long donacionId,
                                                       String nombreEntidad) {
    return enviar(donante, TipoEvento.DONACION_ASIGNADA_DONANTE, String.format(
        "Hola %s, tu donacion (ID: %s) fue asignada a %s. Gracias por tu generosidad!",
        donante.nombreParaMostrar(), donacionId, nombreEntidad));
  }

  public Notificacion notificarDonacionAsignadaBeneficiario(Destinatario beneficiario,
                                                            String nombreEntidad,
                                                            Long donacionId) {
    return enviar(beneficiario, TipoEvento.DONACION_ASIGNADA_BENEFICIARIO, String.format(
        "Estimada/o %s, se les ha asignado una donacion (ID: %s) acorde a sus "
            + "necesidades registradas. Proximamente recibiran informacion sobre la entrega.",
        nombreEntidad, donacionId));
  }

  // ---------- 4. Inicio de ruta ----------

  public void notificarInicioRuta(List<Destinatario> destinatarios,
                                  String rutaId,
                                  String urlMapa) {
    String enlace = (urlMapa != null && !urlMapa.isBlank())
        ? urlMapa
        : "disponible proximamente en la plataforma";

    String mensaje = String.format(
        "La ruta de entrega %s ha comenzado. Podes seguir el camion en tiempo real aqui: %s",
        rutaId, enlace);

    destinatarios.forEach(destinatario -> enviarSeguro(destinatario, TipoEvento.INICIO_RUTA, mensaje));
  }

  // ---------- 5. Entrega exitosa ----------

  public void notificarEntregaExitosa(List<Destinatario> destinatarios,
                                      Long donacionId,
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

    destinatarios.forEach(destinatario -> enviarSeguro(destinatario, TipoEvento.ENTREGA_EXITOSA, mensaje));
  }

  // ---------- 6. Entrega no satisfactoria ----------

  public void notificarEntregaFallida(List<Destinatario> destinatarios,
                                      Long donacionId,
                                      String motivo,
                                      boolean replanificable) {
    String motivoTexto = (motivo != null && !motivo.isBlank()) ? motivo : "no especificado";

    String mensajeInvolucrados = String.format(
        "La entrega de la donacion (ID: %s) no pudo concretarse. Motivo: %s.%s",
        donacionId, motivoTexto,
        replanificable
            ? " La entrega sera replanificada."
            : " El equipo administrativo revisara el caso.");

    destinatarios.forEach(destinatario -> enviarSeguro(destinatario, TipoEvento.ENTREGA_FALLIDA, mensajeInvolucrados));

    String mensajeAdmins = String.format(
        "[ADMIN] Entrega fallida. Donacion: %s | Motivo: %s | Replanificable: %s",
        donacionId, motivoTexto, replanificable ? "si" : "no");

    AdministradorRepository.Instance.administradores()
        .forEach(admin -> enviarSeguro(admin, TipoEvento.ENTREGA_FALLIDA, mensajeAdmins));
  }

  // ---------- helpers ----------

  private Notificacion enviar(Destinatario destinatario, TipoEvento tipo, String mensaje) {
    Notificacion notificacion = enviador.enviarNotificacionA(destinatario, mensaje);
    notificacion.setTipoEvento(tipo);
    NotificacionRepository.Instance.actualizar(notificacion);
    return notificacion;
  }

  private void enviarSeguro(Destinatario destinatario, TipoEvento tipo, String mensaje) {
    try {
      enviar(destinatario, tipo, mensaje);
    } catch (Exception e) {
      System.err.println("[Notificaciones] No se pudo notificar a "
          + destinatario.nombreParaMostrar() + ": " + e.getMessage());
    }
  }
}