package ar.edu.utn.frba.dds.model.fallaentrega;

public class ImprevistoLogistico implements MotivoFallo{
  @Override
  public boolean esReplanificable() {
    return true;
  }

  @Override
  public String darMotivoFallo() {
    return "Un imprevisto logístico ha imposibilitado la entrega de su donación, pronto será replanificada";
  }
}
