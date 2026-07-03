package ar.edu.utn.frba.dds.api.controllers;

import ar.edu.utn.frba.dds.Bienes.Donacion;
import ar.edu.utn.frba.dds.Bienes.DonacionesDominio.DonacionesRepository;
import ar.edu.utn.frba.dds.api.dtos.DonacionDTO;
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

@RestController
@RequestMapping("/api/donaciones")
public class DonacionesController {

  private final DonacionesRepository donacionesRepository;

  public DonacionesController(DonacionesRepository donacionesRepository) {
    this.donacionesRepository = donacionesRepository;
  }

  @GetMapping
  public ResponseEntity<List<Donacion>> listarDonaciones() {
    return ResponseEntity.ok(donacionesRepository.obtenerTodas());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Donacion> obtenerDonacion(@PathVariable String id) {
    Donacion donacion = donacionesRepository.obtenerPorId(id);
    if (donacion == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(donacion);
  }

  @PostMapping
  public ResponseEntity<Donacion> crearDonacion(@RequestBody DonacionDTO dto) {
    Donacion donacion = dto.convertirDtoAObjeto();
    donacionesRepository.agregarDonacion(donacion);
    return ResponseEntity.status(HttpStatus.CREATED).body(donacion);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> actualizarDonacion(@PathVariable String id) {
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminarDonacion(@PathVariable String id) {
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
  }
}
