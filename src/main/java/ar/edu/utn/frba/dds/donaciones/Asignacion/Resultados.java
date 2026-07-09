package ar.edu.utn.frba.dds.donaciones.Asignacion;

import ar.edu.utn.frba.dds.donaciones.entidad.EntidadBeneficiaria;

import java.util.List;

public class Resultados {

  private List<EntidadBeneficiaria> coincidencias;
  private List<EntidadBeneficiaria> resultadosDeCompatibilidad;
  private List<EntidadBeneficiaria> resultadosDeSubAtendidos;
  private boolean huboCoincidencia;

  public Resultados(List<EntidadBeneficiaria> coincidencias,List<EntidadBeneficiaria> resultadosDeCompatibilidad, List<EntidadBeneficiaria> resultadosDeSubAtendidos) {
    this.coincidencias = coincidencias;
    this.resultadosDeCompatibilidad = resultadosDeCompatibilidad;
    this.resultadosDeSubAtendidos = resultadosDeSubAtendidos;
    this.huboCoincidencia = !coincidencias.isEmpty();
  }

  public List<EntidadBeneficiaria> entidadesPropuestas() {
    return huboCoincidencia ? coincidencias : resultadosDeCompatibilidad;
  }

}
