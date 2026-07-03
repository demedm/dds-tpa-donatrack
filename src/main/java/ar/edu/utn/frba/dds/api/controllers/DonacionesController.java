package ar.edu.utn.frba.dds.api.controllers;

import ar.edu.utn.frba.dds.Bienes.Donacion;
import ar.edu.utn.frba.dds.api.dtos.DonacionDTO;
import ar.edu.utn.frba.dds.api.repository.RegistroDonacion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/donaciones")
public class DonacionesController {

  @GetMapping
  public ResponseEntity<List<Donacion>> listarDonaciones() {

    return ResponseEntity.ok(RegistroDonacion.getDonaciones());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Donacion> getDonaciones(@PathVariable String id) {

    Optional<Donacion> donacion = RegistroDonacion.buscarPorId(id);
    if (donacion.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(donacion.get());  }

  @PostMapping
  public ResponseEntity<Donacion> crearDonacion(@RequestBody DonacionDTO dto) {

    Donacion donacion = dto.convertirDtoAObjeto();
    RegistroDonacion.registrar(donacion);
    return ResponseEntity.status(HttpStatus.CREATED).body(donacion);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> actualizarDonacion(@PathVariable Long id) {

    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminarDonacion(@PathVariable Long id) {

    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
  }


}
