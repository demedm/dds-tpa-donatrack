package ar.edu.utn.frba.dds.necesidad;
import ar.edu.utn.frba.dds.Bienes.Donacion;
import ar.edu.utn.frba.dds.Bienes.DonacionSegmentada;
import ar.edu.utn.frba.dds.Bienes.Bien;
import java.util.ArrayList;
import java.util.List;

public class ResultadoBusqueda {
  private int restante;
  private List<Bien> bienesAsignados;
  private List<DonacionSegmentada> donacionesAsignadas = new ArrayList<>();

  public ResultadoBusqueda(int restante, List<Bien> bienesAsignados) {
    this.restante = restante;
    this.bienesAsignados = bienesAsignados;

  }
  public List<DonacionSegmentada> getDonacionesAsignadas() {
    return donacionesAsignadas;
  }
  public void agregarDonacionesAsignadas(List<DonacionSegmentada> donaciones) {
    this.donacionesAsignadas.addAll(donaciones);
  }
  public int getRestante() { return restante; }
  public List<Bien> getBienesAsignados() { return bienesAsignados; }
}
