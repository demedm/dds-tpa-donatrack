package ar.edu.utn.frba.dds.rutas;

public class Routes {

  public void configure() {
    new DonacionRutas().registrar();
    new MatchmakingRutas().registrar();
    new DonanteRutas().registrar();
    new EntidadRutas().registrar();
  }
}
