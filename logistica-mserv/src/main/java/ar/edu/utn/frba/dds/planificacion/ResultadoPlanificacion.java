package ar.edu.utn.frba.dds.planificacion;

import java.util.ArrayList;
import java.util.List;

public class ResultadoPlanificacion {
  public boolean yaProcesado;                                 // callback repetido o lote no vigente
  public final List<Long> asignadas = new ArrayList<>();      // donacionIds
  public final List<Long> reintentables = new ArrayList<>();  // vuelven a PENDIENTE
  public final List<Long> noPlanificables = new ArrayList<>();// se agotó la planificación
}
