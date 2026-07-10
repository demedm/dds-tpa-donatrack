package ar.edu.utn.frba.dds.model.fallaentrega;

public class NoRecepcionada implements MotivoFallo{
  @Override
  public boolean esReplanificable() {
    return true;
  }

  @Override
  public String darMotivoFallo() {
    return "La entrega no fue recibida por la entidad beneficiaria y su entrega será replanificada";
  }
}
