package ar.edu.utn.frba.dds.logistica.AccionesRutas;

import ar.edu.utn.frba.dds.logistica.AccionesSobreRutas;
import ar.edu.utn.frba.dds.modelos.Ruta;

public class NotificarSobreRuta implements AccionesSobreRutas {
  // Notificacion por entrega no satisfactoria
  @Override
  public void actualizarRuta(Ruta ruta, boolean asignada) {
    if (!asignada) {
      //new Notificador().enviarNotificacionA(ruta., // solo conozco el mail de la persona
      //    "Su entrega fue cancelada y será replanificada después de revisar el motivo");
    }
  }
}
