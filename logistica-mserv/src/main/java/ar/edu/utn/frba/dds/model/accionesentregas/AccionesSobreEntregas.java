package ar.edu.utn.frba.dds.model.accionesentregas;

import ar.edu.utn.frba.dds.model.Entrega;

public interface AccionesSobreEntregas {
  void notificarInicioRuta(Entrega entrega);
  void notificarFalloEntrega(Entrega entrega);
}
