package ar.edu.utn.frba.dds.api.controllers;

import ar.edu.utn.frba.dds.api.dtos.NecesidadDTO;
import ar.edu.utn.frba.dds.necesidad.NecesidadDominio.Necesidad;
import ar.edu.utn.frba.dds.necesidad.NecesidadDominio.NecesidadRepository;
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
@RequestMapping("/api/necesidades")
public class NecesidadMaterialesController {

  private final NecesidadRepository necesidadRepository;

  public NecesidadMaterialesController(NecesidadRepository necesidadRepository) {
    this.necesidadRepository = necesidadRepository;
  }

  @GetMapping
  public ResponseEntity<List<Necesidad>> listarNecesidad() {
    return ResponseEntity.ok(necesidadRepository.obtenerTodas());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Necesidad> obtenerNecesidad(@PathVariable String id) {
    Necesidad necesidad =necesidadRepository.obtenerPorId(id);

    if (necesidad == null){
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(necesidad);
  }

  @PostMapping
  public ResponseEntity<Necesidad> crearNecesidad(@RequestBody NecesidadDTO dto) {
    Necesidad necesidad = dto.convertirDtoAObjeto();

    necesidadRepository.agregarNecesidad(necesidad);
    return ResponseEntity.status(HttpStatus.CREATED).body(necesidad);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Necesidad> actualizarNecesidad(@PathVariable String id, @RequestBody NecesidadDTO dto) {
    Necesidad necesidad = dto.convertirDtoAObjeto();
    necesidad.setId(id);
    return ResponseEntity.ok(necesidadRepository.actualizar(necesidad));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminarNecesidad(@PathVariable String id) {
    necesidadRepository.eliminar(id);
    return ResponseEntity.ok().build();
  }
  }
