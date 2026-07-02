package ar.edu.utn.frba.dds.Bienes;

import ar.edu.utn.frba.dds.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.Estado.EnDeposito;
import ar.edu.utn.frba.dds.Estado.EstadoDonacion;

import java.util.List;

public class DonacionSegmentada {
  private int cantidad;
  private Subcategoria subcategoria;
  private List<ar.edu.utn.frba.dds.Bienes.Bien> bienesFiltrados;
  private EstadoDonacion estado;

  public DonacionSegmentada(Integer cantidad, Subcategoria subcategoria, List<ar.edu.utn.frba.dds.Bienes.Bien> bienesFiltrados) {
    this.cantidad = cantidad;
    this.subcategoria = subcategoria;
    this.bienesFiltrados = bienesFiltrados;
    this.estado = EnDeposito.asignar() ;
  }



  public void donar(EntidadBeneficiaria entidad ){

  }
}
