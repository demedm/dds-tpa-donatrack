package ar.edu.utn.frba.dds.donaciones.Asignacion;

import ar.edu.utn.frba.dds.donaciones.Bienes.DonacionSegmentada;
import ar.edu.utn.frba.dds.donaciones.entidad.EntidadBeneficiaria;

import java.util.List;

public interface AlgoritmoAsignacion {

  List<EntidadBeneficiaria> obtenerRanking(DonacionSegmentada donacion, List<EntidadBeneficiaria> entidades);


}
