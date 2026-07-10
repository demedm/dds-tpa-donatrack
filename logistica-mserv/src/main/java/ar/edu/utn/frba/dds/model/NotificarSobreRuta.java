package ar.edu.utn.frba.dds.model;

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
