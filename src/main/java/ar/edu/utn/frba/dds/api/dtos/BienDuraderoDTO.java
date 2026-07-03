package ar.edu.utn.frba.dds.api.dtos;

import ar.edu.utn.frba.dds.Bienes.BienDuradero;
import ar.edu.utn.frba.dds.Bienes.EstadoUso;
import ar.edu.utn.frba.dds.Bienes.Subcategoria;

public class BienDuraderoDTO {

  public Subcategoria subcategoria;
  public String foto;
  public String descripcion;
  public EstadoUso estadoUso;

  public BienDuradero convertirDtoAObjeto() {
    return new BienDuradero(subcategoria, foto, descripcion, estadoUso);
  }
}
