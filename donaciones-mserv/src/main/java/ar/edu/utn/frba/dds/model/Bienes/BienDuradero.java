package ar.edu.utn.frba.dds.model.Bienes;

public class BienDuradero extends Bien {
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
