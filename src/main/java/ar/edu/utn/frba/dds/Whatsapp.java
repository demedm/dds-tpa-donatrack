package ar.edu.utn.frba.dds;

public class Whatsapp implements MedioContacto {
  private String info;

  public Whatsapp(String numeroTelefono) {
    this.info = numeroTelefono;
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
