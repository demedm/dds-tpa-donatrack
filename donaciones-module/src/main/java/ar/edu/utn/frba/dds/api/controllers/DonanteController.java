package ar.edu.utn.frba.dds.api.controllers;

import ar.edu.utn.frba.dds.RegistroDonante;
import ar.edu.utn.frba.dds.donantes.Persona;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


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

