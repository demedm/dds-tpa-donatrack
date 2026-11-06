package ar.edu.utn.frba.dds.dto.salida;

import ar.edu.utn.frba.dds.dto.EntidadDTO;
import ar.edu.utn.frba.dds.model.Asignacion.EjecucionAlgoritmo;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class PropuestasSalidaDTO {

  public Long donacionSegmentadaId;
  public boolean huboCoincidencias;
  public List<EntidadDTO> propuestas;                 // coincidencias, o todas si no hubo
  public Map<String, List<EntidadDTO>> porAlgoritmo;  // cada ejecución, en orden

  // buscarEntidad: de un id a su DTO (lo resuelve el controller con el repositorio)
  public static PropuestasSalidaDTO desde(DonacionSegmentada segmentada,
                                          Function<Long, EntidadDTO> buscarEntidad) {
    PropuestasSalidaDTO dto = new PropuestasSalidaDTO();
    dto.donacionSegmentadaId = segmentada.getId();
    dto.huboCoincidencias = segmentada.huboCoincidencias();
    dto.propuestas = aEntidades(segmentada.getIdsEntidadesPropuestas(), buscarEntidad);
    dto.porAlgoritmo = new LinkedHashMap<>();
    for (EjecucionAlgoritmo ejecucion : segmentada.getEjecuciones()) {
      dto.porAlgoritmo.put(ejecucion.getAlgoritmo(), aEntidades(ejecucion.getIdsEntidades(), buscarEntidad));
    }
    return dto;
  }

  private static List<EntidadDTO> aEntidades(List<Long> ids, Function<Long, EntidadDTO> buscarEntidad) {
    return ids.stream().map(buscarEntidad).filter(Objects::nonNull).toList();
  }
}