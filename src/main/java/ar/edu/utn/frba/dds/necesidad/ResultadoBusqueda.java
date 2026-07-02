package ar.edu.utn.frba.dds.necesidad;

import ar.edu.utn.frba.dds.Bienes.Bien;

import java.util.List;

public class ResultadoBusqueda {
  private int restante;
  private List<Bien> bienesAsignados;

  public ResultadoBusqueda(int restante, List<Bien> bienesAsignados) {
    this.restante = restante;
    this.bienesAsignados = bienesAsignados;
  }

  public int getRestante() { return restante; }
  public List<Bien> getBienesAsignados() { return bienesAsignados; }
}
