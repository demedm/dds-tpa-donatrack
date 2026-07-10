package ar.edu.utn.frba.dds.model.Asignacion;

import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.Estado.EnDeposito;

import java.util.List;

public class ServicioMatchmaking {

  private AlgoritmoDeCompatibilidad algoritmo1;
  private AlgoritmoSubatendidos algoritmo2;

  public Resultados ejecutar(DonacionSegmentada donacion, List<EntidadBeneficiaria> entidades){

    if(donacion.estaAlmacen()){
      throw new IllegalStateException("Solo se puede asignar donaciones en estado EN_DEPOSITO");
    }

    List<EntidadBeneficiaria> resultadoCompatibilidad = algoritmo1.obtenerRanking(donacion,entidades);
    List<EntidadBeneficiaria> resultadosSubatendidos = algoritmo2.obtenerRanking(donacion,entidades);

    List<EntidadBeneficiaria> coincidencias = resultadoCompatibilidad.stream()
        .filter(resultadosSubatendidos::contains)
        .toList();

    return new Resultados(coincidencias,resultadoCompatibilidad,resultadosSubatendidos);

  }
}
