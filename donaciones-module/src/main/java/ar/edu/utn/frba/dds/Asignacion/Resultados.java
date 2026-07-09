package ar.edu.utn.frba.dds.Asignacion;

import ar.edu.utn.frba.dds.entidad.EntidadBeneficiaria;

import java.util.List;

public class Resultados {

  private final List<EntidadBeneficiaria> coincidencias;
  private final List<EntidadBeneficiaria> resultadoDeCompatibilidad;
  private final List<EntidadBeneficiaria> resultadoDeSubAtendidos;
  private final boolean huboCoincidencia;

  public Resultados(List<EntidadBeneficiaria> coincidencias,List<EntidadBeneficiaria> resultadosDeCompatibilidad, List<EntidadBeneficiaria> resultadosDeSubAtendidos) {
    this.coincidencias = coincidencias;
    this.resultadoDeCompatibilidad = resultadosDeCompatibilidad;
    this.resultadoDeSubAtendidos = resultadosDeSubAtendidos;
    this.huboCoincidencia = !coincidencias.isEmpty();
  }

  public List<EntidadBeneficiaria> entidadesPropuestas() {
    return huboCoincidencia ? coincidencias : resultadoDeCompatibilidad;
  }

}
