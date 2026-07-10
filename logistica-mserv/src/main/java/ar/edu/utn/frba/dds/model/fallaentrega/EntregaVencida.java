package ar.edu.utn.frba.dds.model.fallaentrega;

public class EntregaVencida implements MotivoFallo{
  @Override
  public boolean esReplanificable() {
    return false;
  }

  @Override
  public String darMotivoFallo() {
    return "La entrega falló porque la donación ha pasado su fecha de vencimiento";
  }
}
