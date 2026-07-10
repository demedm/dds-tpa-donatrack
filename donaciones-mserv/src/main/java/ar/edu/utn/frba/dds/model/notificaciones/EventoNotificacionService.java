package ar.edu.utn.frba.dds.model.notificaciones;

import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;
import ar.edu.utn.frba.dds.repositories.NotificacionRepository;
import ar.edu.utn.frba.dds.model.medioscontacto.Mail;

public class EventoNotificacionService {

    /**
     * Instancia lista para usar desde los controllers, con envío asincrónico
     * por defecto (para no bloquear el request HTTP que dispara la notificación).
     */
    public static final EventoNotificacionService Instance =
        new EventoNotificacionService(new NotificadorAsincronico(NotificacionRepository.Instance));

    private final EnviadorNotificaciones enviador;

    public EventoNotificacionService(EnviadorNotificaciones enviador) {
      this.enviador = enviador;
    }

    /** 1. Donante sin actividad por más de 20 días. */
    public Notificacion notificarInactividadDonante(Persona donante) {
      String mensaje = String.format(
          "Hola %s, te extrañamos en DonaTrack. "
              + "Han pasado más de 20 días desde tu última actividad. "
              + "¿Querés realizar una nueva donación? Ingresá a la plataforma.",
          donante.getNombreIdentificador());

      return etiquetar(enviador.enviarNotificacionA(donante, mensaje), TipoEvento.INACTIVIDAD_DONANTE);
    }

    /** 3. Notificación al donante cuando su donación fue asignada. */
    public Notificacion notificarDonacionAsignadaDonante(Persona donante,
                                                         String donacionId, String nombreEntidad) {
      String mensaje = String.format(
          "Hola %s, tu donación (ID: %s) fue asignada a %s. ¡Gracias por tu generosidad!",
          donante.getNombreIdentificador(), donacionId, nombreEntidad);

      return etiquetar(enviador.enviarNotificacionA(donante, mensaje),
          TipoEvento.DONACION_ASIGNADA_DONANTE);
    }

    /** 2. Notificación a una entidad beneficiaria cuando se le asigna una donación. */
    public Notificacion notificarDonacionAsignadaBeneficiario(MedioContacto medioEntidad,
                                                              String nombreEntidad, String donacionId) {
      String mensaje = String.format(
          "Estimada/o %s, se les ha asignado una donación (ID: %s) "
              + "acorde a sus necesidades registradas. "
              + "Próximamente recibirán información sobre la entrega.",
          nombreEntidad, donacionId);

      return etiquetar(enviador.enviarNotificacionA(medioEntidad, mensaje),
          TipoEvento.DONACION_ASIGNADA_BENEFICIARIO);
    }

    /** 4. Inicio de ruta — incluye URL del mapa en tiempo real, si se conoce. */
    public Notificacion notificarInicioRuta(Persona persona, String rutaId, String urlMapa) {
      String enlace = (urlMapa != null) ? urlMapa : "disponible próximamente en la plataforma";
      String mensaje = String.format(
          "La ruta de entrega %s ha comenzado. Podés seguir el camión en tiempo real aquí: %s",
          rutaId, enlace);

      return etiquetar(enviador.enviarNotificacionA(persona, mensaje), TipoEvento.INICIO_RUTA);
    }

    /** 4. Variante para destinatarios que no son Persona (entidad beneficiaria). */
    public Notificacion notificarInicioRuta(MedioContacto medio, String rutaId, String urlMapa) {
      String enlace = (urlMapa != null) ? urlMapa : "disponible próximamente en la plataforma";
      String mensaje = String.format(
          "La ruta de entrega %s ha comenzado. Podés seguir el camión en tiempo real aquí: %s",
          rutaId, enlace);

      return etiquetar(enviador.enviarNotificacionA(medio, mensaje), TipoEvento.INICIO_RUTA);
    }

    /** 5. Entrega confirmada exitosamente — incluye comprobante. */
    public Notificacion notificarEntregaExitosa(Persona persona,
                                                String donacionId, String fechaHora, String patenteCamion) {
      String mensaje = String.format(
          "La donación (ID: %s) fue entregada exitosamente. "
              + "Fecha y hora: %s | Camión: %s. ¡Muchas gracias!",
          donacionId, fechaHora, patenteCamion != null ? patenteCamion : "no especificado");

      return etiquetar(enviador.enviarNotificacionA(persona, mensaje), TipoEvento.ENTREGA_EXITOSA);
    }

    /** 5. Variante para la entidad beneficiaria. */
    public Notificacion notificarEntregaExitosa(MedioContacto medio,
                                                String donacionId, String fechaHora, String patenteCamion) {
      String mensaje = String.format(
          "La donación (ID: %s) fue entregada exitosamente. "
              + "Fecha y hora: %s | Camión: %s. ¡Muchas gracias por confirmar la recepción!",
          donacionId, fechaHora, patenteCamion != null ? patenteCamion : "no especificado");

      return etiquetar(enviador.enviarNotificacionA(medio, mensaje), TipoEvento.ENTREGA_EXITOSA);
    }

    /** 6. Entrega fallida — para destinatarios que son Persona (donante). */
    public Notificacion notificarEntregaFallida(Persona persona, String donacionId, String motivo) {
      String mensaje = String.format(
          "La entrega de la donación (ID: %s) no pudo concretarse. "
              + "Motivo: %s. El equipo administrativo revisará el caso "
              + "y coordinará una nueva asignación si corresponde.",
          donacionId, motivo != null ? motivo : "no especificado");

      return etiquetar(enviador.enviarNotificacionA(persona, mensaje), TipoEvento.ENTREGA_FALLIDA);
    }

    /** 6. Variante para destinatarios que no son Persona (entidad beneficiaria). */
    public Notificacion notificarEntregaFallida(MedioContacto medio, String donacionId, String motivo) {
      String mensaje = String.format(
          "La entrega de la donación (ID: %s) no pudo concretarse. "
              + "Motivo: %s. El equipo administrativo revisará el caso.",
          donacionId, motivo != null ? motivo : "no especificado");

      return etiquetar(enviador.enviarNotificacionA(medio, mensaje), TipoEvento.ENTREGA_FALLIDA);
    }

    /**
     * 6. Notificación a personas administradoras. Como el sistema todavía no
     * modela una entidad "Administrador" (no hay altas/bajas de admins, ni
     * medio de contacto propio), se resuelve con una casilla fija configurada
     * por variable de entorno. Si no está configurada, se deja constancia en
     * el log del servidor en lugar de fallar el resto del flujo.
     */
    public void notificarEntregaFallidaAdmins(String donacionId, String motivo) {
      String mailAdmin = System.getenv("ADMIN_MAIL");
      String mensaje = String.format(
          "[Entrega no satisfactoria] Donación %s. Motivo: %s. Requiere revisión administrativa.",
          donacionId, motivo != null ? motivo : "no especificado");

      if (mailAdmin == null || mailAdmin.isBlank()) {
        System.out.println("[Notificaciones][ADMIN] " + mensaje
            + " (no hay ADMIN_MAIL configurado, se deja constancia solo en el log)");
        return;
      }

      etiquetar(enviador.enviarNotificacionA(new Mail(mailAdmin), mensaje), TipoEvento.ENTREGA_FALLIDA);
    }

    private Notificacion etiquetar(Notificacion notificacion, TipoEvento tipo) {
      notificacion.setTipoEvento(tipo);
      return notificacion;
    }
  }
