package ar.edu.utn.frba.dds.api.controllers;

import ar.edu.utn.frba.dds.RegistroDonante;
import ar.edu.utn.frba.dds.api.dtos.PersonaDTO;
import ar.edu.utn.frba.dds.api.dtos.PersonaFisicaDTO;
import ar.edu.utn.frba.dds.api.dtos.PersonaJuridicaDTO;
import ar.edu.utn.frba.dds.donantes.Persona;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/donantes")
public class DonanteController  {

  private final RegistroDonante registroDonante;

  public DonanteController(RegistroDonante registroDonante) {
    this.registroDonante = registroDonante;
  }

  @PostMapping("/fisica")
  public ResponseEntity<Persona> crearDonantePersonaFisica(@RequestBody PersonaFisicaDTO dto) {
    Persona donanteCreado = RegistroDonante.registrarDonante(dto.toDomain());
    return ResponseEntity.status(HttpStatus.CREATED).body(donanteCreado);
  }

  @PostMapping("/juridica")
  public ResponseEntity<Persona> crearDonantePersonaJuridica(@RequestBody PersonaJuridicaDTO dto) {
    Persona donanteCreado = RegistroDonante.registrarDonante(dto.toDomain());
    return ResponseEntity.status(HttpStatus.CREATED).body(donanteCreado);
  }

  @GetMapping
  public ResponseEntity<List<Persona>> listarDonantes() {
    return ResponseEntity.ok(registroDonante.getDonantes());
  }

  @GetMapping("/{email}")
  public ResponseEntity<Persona> obtenerDonante(@PathVariable String email) {
    return registroDonante.buscarPorEmail(email)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{email}")
  public ResponseEntity<Void> actualizarDatosDonantes(@PathVariable String email, @RequestBody PersonaDTO datos) {
    try {
      RegistroDonante.actualizarDonante(email, datos.toDomain());
      return ResponseEntity.ok().build();
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }
}

