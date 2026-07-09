package ar.edu.utn.frba.dds.logistica.controllers;

/*
@RestController
@RequestMapping("/api/logistica/camiones")
public class MonitoreoController {

  private final Flota flota;

  // Inyección de la flota a través del constructor
  public MonitoreoController(Flota flota) {
    this.flota = flota;
  }

  // 1. Endpoint POST: Recibe la telemetría enviada por el dispositivo GPS físico
  @PostMapping("/{patente}/telemetria")
  public ResponseEntity<String> recepcionarTelemetria(
      @PathVariable String patente,
      @RequestBody TelemetriaDTO telemetria) {

    if (!patente.equals(telemetria.getPatente())) {
      return ResponseEntity.badRequest().body("Inconsistencia en los datos de la patente.");
    }

    Camion camion = flota.buscarCamionPorPatente(patente);
    if (camion == null) {
      return ResponseEntity.notFound().build();
    }

    camion.actualizarUbicacion(telemetria.getLatitud(), telemetria.getLongitud());
    return ResponseEntity.ok("Ubicación actualizada correctamente.");
  }

  // 2. Endpoint GET: Devuelve la información de la flota procesada para el Dashboard
  @GetMapping
  public ResponseEntity<List<CamionDashboardDTO>> obtenerCamionesParaDashboard() {

    // Convertimos la lista de camiones del dominio a DTOs livianos para la vista
    List<CamionDashboardDTO> camionesDashboard = flota.getCamiones().stream()
        .map(camion -> {
          CamionDashboardDTO dto = new CamionDashboardDTO();
          dto.setPatente(camion.getPatente());
          dto.setEstado(camion.getEstado()); // Asigna directamente tu Enum EstadoCamion

          // Si el camión ya cuenta con un reporte de GPS, mapeamos sus datos geográficos
          if (camion.getUbicacionActual() != null) {
            dto.setLatitud(camion.getUbicacionActual().getLatitud());
            dto.setLongitud(camion.getUbicacionActual().getLongitud());
            dto.setUltimaActualizacion(camion.getUbicacionActual().getTimestamp());
          }
          return dto;
        })
        .collect(Collectors.toList());

    return ResponseEntity.ok(camionesDashboard);
  }
}

 */