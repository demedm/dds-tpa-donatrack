package ar.edu.utn.frba.dds.dto;

import ar.edu.utn.frba.dds.model.Asignacion.ServicioMatchmaking;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;

import java.util.List;

public class ResultadosMatchmakingDTO {

  private String donacionSegmentadId;
  private String subcategoria;
  private boolean huboCoincidencia;

  private List<EntidadBeneficiaria> coincidencias;
  private List<EntidadBeneficiaria> entidadesPropuestas;

  private List<EntidadBeneficiaria> resultadoDeCompatibilidad;
  private List<EntidadBeneficiaria> resultadoDeSubAtendidos;




}
