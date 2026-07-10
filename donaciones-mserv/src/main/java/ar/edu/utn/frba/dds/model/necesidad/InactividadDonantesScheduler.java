package ar.edu.utn.frba.dds.model.necesidad;
/*
@Component
public class InactividadDonantesScheduler {

  private static final Logger log = LoggerFactory.getLogger(InactividadDonantesScheduler.class);

  private final EventoNotificacionService eventoService;
  private final RegistroDonante registroDonante;

  @Value("${notificaciones.inactividad-dias:20}")
  private int diasInactividad;

  public InactividadDonantesScheduler(EventoNotificacionService eventoService,
                                      RegistroDonante registroDonante) {
    this.eventoService = eventoService;
    this.registroDonante = registroDonante;
  }

  /**
   * Se ejecuta todos los días a las 02:00 AM.
   * cron = "segundos minutos horas día-mes mes día-semana"
   *
   * Para pruebas locales se puede cambiar a: @Scheduled(fixedDelay = 60000)
   *
  @Scheduled(cron = "0 0 2 * * *")
  public void notificarDonantesInactivos() {
    log.info("[SCHEDULER] Buscando donantes sin actividad hace más de {} días...", diasInactividad);

    LocalDate limiteInactividad = LocalDate.now().minusDays(diasInactividad);

    List<Persona> inactivos = registroDonante.buscarInactivosDesde(limiteInactividad);

    if (inactivos.isEmpty()) {
      log.info("[SCHEDULER] No hay donantes inactivos para notificar.");
      return;
    }

    int enviadas = 0;
    int fallidas = 0;

    for (Persona donante : inactivos) {
      try {
        eventoService.notificarInactividadDonante(donante);
        enviadas++;
      } catch (Exception e) {
        fallidas++;
        log.error("[SCHEDULER] Error al notificar a '{}': {}",
            donante.getNombreIdentificador(), e.getMessage());
      }
    }

    log.info("[SCHEDULER] Completado. Enviadas: {}, Fallidas: {}", enviadas, fallidas);
  }
}
*/
