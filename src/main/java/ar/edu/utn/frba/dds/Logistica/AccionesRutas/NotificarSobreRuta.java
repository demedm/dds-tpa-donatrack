package ar.edu.utn.frba.dds.Logistica.AccionesRutas;

import ar.edu.utn.frba.dds.Logistica.AccionesSobreRutas;
import ar.edu.utn.frba.dds.Logistica.Ruta;
import ar.edu.utn.frba.dds.Notificador;

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
