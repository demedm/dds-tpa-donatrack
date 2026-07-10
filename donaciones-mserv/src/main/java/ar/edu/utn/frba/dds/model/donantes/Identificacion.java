package ar.edu.utn.frba.dds.model.donantes;

public class Identificacion {
  TipoDocumento tipo;
  String nroDocumento;

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
