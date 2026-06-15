package ar.edu.utn.frba.dds.medioscontacto;

import ar.edu.utn.frba.dds.Notificacion;

public class Mensaje implements MedioContacto {


  @Override
  public String getMedioContacto() {
    return "";
  }

  @Override
  public void contactar(Notificacion notificacion) {
    String numDest = notificacion.getDestinatario();
    String mensaje = notificacion.getMensaje();

    String mockPayload = String.format("{\"para\": \"%s\", \"cuerpo\": \"%s\"}", numDest, mensaje);

    System.out.println("==SIM. DE CONEXION CON API EXTERNA DE WHATSAPP MEDIANTE HTTP==");
    System.out.println("[ENVIO DE PAYLOAD]: " + mockPayload);
    System.out.println("[HTTP STATUS: Ok ] : MENSAJE ENTREGADO CON EXITO. ");

    notificacion.marcarComoCompletada();
  }

}
