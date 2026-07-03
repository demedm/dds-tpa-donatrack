package ar.edu.utn.frba.dds.api.dtos;

import ar.edu.utn.frba.dds.Bienes.BienPerecedero;
import ar.edu.utn.frba.dds.Bienes.Subcategoria;

import java.util.Date;

public class BienPerecederoDTO {

  public Subcategoria subcategoria;
  public String foto;
  public String descripcion;
  public Date fechaVencimiento;

  public BienPerecedero convertirDtoAObjeto() {
    return new BienPerecedero(subcategoria, foto, descripcion, fechaVencimiento);
  }
}
