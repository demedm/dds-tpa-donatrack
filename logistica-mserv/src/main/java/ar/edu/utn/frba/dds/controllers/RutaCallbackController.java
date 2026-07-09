package ar.edu.utn.frba.dds.controllers;

/*
@RestController
@RequestMapping("/api/logistica/rutas")
public class RutaCallbackController {

  private final GestorRutas gestorRutas;

  public RutaCallbackController(GestorRutas gestorRutas) {
    this.gestorRutas = gestorRutas;
  }

  @PostMapping("/callback")
  public ResponseEntity<String> recibirResultadoPlanificacion(
      @RequestBody PlanificacionRutasResponse respuesta) {

    if (respuesta == null) {
      return ResponseEntity.badRequest().body("La respuesta de planificación no puede ser nula.");
    }

    try {
      gestorRutas.recibirRespuesta(respuesta);

      return ResponseEntity.ok("Planificación de rutas recibida e impactada correctamente.");

    } catch (Exception e) {
      return ResponseEntity.internalServerError()
          .body("Error interno al procesar las rutas en el sistema core: " + e.getMessage());
    }
  }
}

 */