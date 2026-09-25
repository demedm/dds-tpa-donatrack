package ar.edu.utn.frba.dds.model.donantes;
import javax.persistence.*;

@Embeddable
public class Identificacion {
  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_documento")
  private TipoDocumento tipo;

  @Column(name = "nro_documento")
  private String nroDocumento;
  public Identificacion() {}
  public Identificacion(TipoDocumento tipo, String nro) {
    this.tipo = tipo;
    this.nroDocumento = nro;
  }

  public String getNroDocumento() {
    return nroDocumento;
  }

  public TipoDocumento getTipo() {
    return tipo;
  }

  public void setNroDocumento(String nroDocumento) {
    this.nroDocumento = nroDocumento;
  }
}
