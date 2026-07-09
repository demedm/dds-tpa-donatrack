package ar.edu.utn.frba.dds.model.Asignacion;

import ar.edu.utn.frba.dds.model.Bienes.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;

import java.util.List;

public interface AlgoritmoAsignacion {

  List<EntidadBeneficiaria> obtenerRanking(DonacionSegmentada donacion, List<EntidadBeneficiaria> entidades);


}
