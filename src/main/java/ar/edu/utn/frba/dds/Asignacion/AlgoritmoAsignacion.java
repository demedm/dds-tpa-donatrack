package ar.edu.utn.frba.dds.Asignacion;

import ar.edu.utn.frba.dds.Bienes.DonacionSegmentada;
import ar.edu.utn.frba.dds.EntidadBeneficiaria;

import java.util.List;

public interface AlgoritmoAsignacion {

  List<EntidadBeneficiaria> obtenerRanking(DonacionSegmentada donacion, List<EntidadBeneficiaria> entidades);


}
