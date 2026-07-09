package ar.edu.utn.frba.dds.donaciones.Bienes;

public class BienDuradero extends ar.edu.utn.frba.dds.donaciones.Bienes.Bien {
  private EstadoUso estado;

  public BienDuradero(Subcategoria subCategoria, String foto, String descripcion, EstadoUso estado) {
    super(subCategoria, foto, descripcion);
    this.estado = estado;
  }

  @Override
  public Object getCriterioSegmentacion() {
    return this.estado;
  }

}
