package ar.edu.utn.frba.dds.model.fallaentrega;

public enum MotivoFallo {
  ENTREGA_VENCIDA(false, "La donación ha pasado su fecha de vencimiento"),
  IMPREVISTO_LOGISTICO(true, "Un imprevisto logístico ha imposibilitado la entrega de la donación"),
  NO_RECEPCIONADA(true, "La entrega no fue recibida por la entidad beneficiaria y su entrega será replanificada");

  private boolean replanificable;
  private String descripcionMotivo;

  MotivoFallo(boolean replanificable, String descripcionMotivo) {
    this.replanificable = replanificable;
    this.descripcionMotivo = descripcionMotivo;
  }

  public boolean esReplanificable() {
    return replanificable;
  }

  public String getDescripcionMotivo() {
    return descripcionMotivo;
  }
}
