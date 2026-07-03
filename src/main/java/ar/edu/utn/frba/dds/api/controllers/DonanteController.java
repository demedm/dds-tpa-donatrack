package ar.edu.utn.frba.dds.api.controllers;

import ar.edu.utn.frba.dds.api.repository.DonanteRepository;
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
import java.util.Optional;


@RestController
@RequestMapping("/api/donantes")
public class DonanteController  {

  private final DonanteRepository donanteRepository;

  public DonanteController(DonanteRepository donanteRepository) {
    this.donanteRepository = donanteRepository;
  }

  @PostMapping("/fisica")
  public ResponseEntity<Persona> crearDonantePersonaFisica(@RequestBody PersonaFisicaDTO dto) {

    Persona donanteCreado = DonanteRepository.registrarDonante(dto.convertirDtoAObjeto());
    return ResponseEntity.status(HttpStatus.CREATED).body(donanteCreado);
  }

  @PostMapping("/juridica")
  public ResponseEntity<Persona> crearDonantePersonaJuridica(@RequestBody PersonaJuridicaDTO dto) {

    Persona donanteCreado = DonanteRepository.registrarDonante(dto.convertirDtoAObjeto());
    return ResponseEntity.status(HttpStatus.CREATED).body(donanteCreado);
  }

  @GetMapping
  public ResponseEntity<List<Persona>> listarDonantes() {

    return ResponseEntity.ok(donanteRepository.getRegistroDonantes());
  }

  @GetMapping("/{email}")
  public ResponseEntity<Persona> obtenerDonante(@PathVariable String email) {

    Optional<Persona> donante = donanteRepository.buscarPorEmail(email);

    if (donante.isPresent()) {
      return ResponseEntity.ok(donante.get());
    }
    return ResponseEntity.notFound().build();
  }

  @PutMapping("/{email}")
  public ResponseEntity<Void> actualizarDatosDonantes(@PathVariable String email, @RequestBody PersonaDTO datos) {

    try {
      DonanteRepository.actualizarDonante(email, datos.convertirDtoAObjeto());
      return ResponseEntity.ok().build();

    }catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }
}

