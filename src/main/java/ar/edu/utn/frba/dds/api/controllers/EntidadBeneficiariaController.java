package ar.edu.utn.frba.dds.api.controllers;

import ar.edu.utn.frba.dds.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.api.repository.RegistroEntidades;
import ar.edu.utn.frba.dds.api.dtos.EntidadBeneficiariaDTO;
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
@RequestMapping("/api/entidades")
public class EntidadBeneficiariaController {

  @GetMapping
  public ResponseEntity<List<EntidadBeneficiaria>> listarEntidades() {
    return ResponseEntity.ok(RegistroEntidades.getEntidades());
  }

  @GetMapping("/{razonSocial}")
  public ResponseEntity<EntidadBeneficiaria> obtenerEntidad(@PathVariable String razonSocial) {

    Optional<EntidadBeneficiaria> entidad = RegistroEntidades.buscarPorRazonSocial(razonSocial);

    if (entidad.isPresent()) {
      return ResponseEntity.ok(entidad.get());
    }
    return ResponseEntity.notFound().build();

  }

  @PostMapping
  public ResponseEntity<EntidadBeneficiaria> crearEntidad(@RequestBody EntidadBeneficiariaDTO dto) {
    EntidadBeneficiaria entidad = dto.convertirDtoAObjeto();

    RegistroEntidades.registrar(entidad);
    return ResponseEntity.status(HttpStatus.CREATED).body(entidad);
  }

  @PutMapping("/{razonSocial}")
  public ResponseEntity<Void> actualizarEntidad(@PathVariable String razonSocial, @RequestBody EntidadBeneficiariaDTO dto) {
    try{
      RegistroEntidades.actualizarRazonSocial(razonSocial, dto);
      return ResponseEntity.ok().build();

    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{razonSocial}")
  public ResponseEntity<Void> eliminarEntidad(@PathVariable String razonSocial) {
    try {

      RegistroEntidades.eliminarRazonSocial(razonSocial);
      return ResponseEntity.ok().build();

    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
