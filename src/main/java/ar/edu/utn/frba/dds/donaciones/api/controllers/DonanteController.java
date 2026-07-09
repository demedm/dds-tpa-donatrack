package ar.edu.utn.frba.dds.donaciones.api.controllers;

import java.util.List;
import java.util.Optional;

/*
@RestController
@RequestMapping("/api/donantes")
public class DonanteController  {

  private final RegistroDonante registroDonante;

  public DonanteController(RegistroDonante registroDonante) {
    this.registroDonante = registroDonante;
  }

  @PostMapping
  public ResponseEntity<Persona> crearDonante(@RequestBody Persona nuevoDonante) {
    Persona donanteCreado = RegistroDonante.registrarDonante(nuevoDonante);
    return ResponseEntity.status(HttpStatus.CREATED).body(donanteCreado);
  }

  @GetMapping
  public List<Persona> listarDonantes() {
    return registroDonante.getDonantes();
  }

  @GetMapping("/{id}")
  public Optional<Persona> obtenerDonante(@PathVariable String email) {
    return registroDonante.buscarPorEmail(email);
  }

}

 */

