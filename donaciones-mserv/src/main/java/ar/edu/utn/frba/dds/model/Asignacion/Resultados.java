package ar.edu.utn.frba.dds.model.Asignacion;

import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;

import java.util.List;

public class Resultados {

  private final List<EntidadBeneficiaria> coincidencias;
  private final List<List<EntidadBeneficiaria>> resultadoPorAlgoritmo;
  private final boolean huboCoincidencia;

  public Resultados(List<EntidadBeneficiaria> coincidencias,
                    List<List<EntidadBeneficiaria>> resultadoPorAlgoritmo) {
    this.coincidencias = coincidencias;
    this.resultadoPorAlgoritmo = resultadoPorAlgoritmo;
    this.huboCoincidencia = !coincidencias.isEmpty();
  }

  public List<EntidadBeneficiaria> entidadesPropuestas() {
    if(this.huboCoincidencia) return coincidencias;

    return resultadoPorAlgoritmo.stream().flatMap(List::stream).distinct().toList();

  }

  public List<List<EntidadBeneficiaria>> getResultadoPorAlgoritmo() {
    return resultadoPorAlgoritmo;
  }


}
